
# WICHTIG: Bei Erstinstallation zuerst install_everything.r _ausführen

source('libs.r')
source('import.r')

#features <- c('Age', 'Sex', 'Fare', 'Pclass', 'SibSp', 'Parch', 'Embarked', 'Cabin', 'CabinClass', 'peopleInCabin')
features <- c('Age', 'Sex', 'Fare', 'Pclass', 'SibSp', 'Parch', 'Embarked')
labels <- 'Survived'

#Traningsdaten
trainCsv <- titanicCsvImport('../data/train.csv')
train <- trainCsv[, c(features, labels)]

#Ein Blick in die Datenstrukturen
str(trainCsv)

# Manuell in Traningsdaten und Validierungsdaten zerlegen
splits <- sample(nrow(trainCsv)*0.85)
train <- trainCsv[splits, c(features, labels)]
test <- trainCsv[-splits, c(features, labels)]

# Start H2O
#h2o.shutdown(prompt = F)
h2o.init()

h2o_train <- as.h2o(train)
h2o_test <- as.h2o(test)

# Trainiere ein oder mehrere Modelle (RF, GBM, XGBOOST)
# Random Forest
titanic_rf <- h2o.randomForest(x=features,
                       y=labels,
                       training_frame = h2o_train,
                       model_id = 'ipv-titanic_rf',
                       nfolds = 10)


# Analyse des tranierten random Forest
h2o.confusionMatrix(titanic_rf)
h2o.auc(titanic_rf)

# Vergleiche den Einfluss der Variablen der Blackbox?
h2o.varimp_plot(titanic_rf)

# Automatisiertes Machinelles Lernen: Modell selbst lernen lassen
titanic_automl <- h2o.automl(x=features,
                       y=labels,
                       seed = 12345,
                       training_frame = h2o_train,
                       max_models=10,
                       nfolds = 5)

# Alle gelernten Modelle betrachten
titanic_leaderboard <- h2o.get_leaderboard(titanic_automl, extra_columns = 'ALL')
print(titanic_leaderboard, n = nrow(titanic_leaderboard))

# Das "beste" Modell
titanic_leader <- titanic_automl@leader
titanic_leader@model_id

h2o_test.prediction = h2o.predict(titanic_leader, h2o_test)
test.prediction = as.data.frame(h2o_test.prediction)

#test.results = merge(test, test.prediction, by.x=row.names(test), by.y=row.names(test.prediction))
test.results = cbind(test, test.prediction)

# Analyse des besten Modells
h2o.confusionMatrix(titanic_leader, h2o_test)



# Anomalieerkennung
# einen Autoencoder tranieren
titanic_anomalie <- h2o.deeplearning(x=features,
                                     seed=12345,
                                     model_id = "aie-titanic_autoencoder",
                                     training_frame = h2o_train,
                                     autoencoder = T,
                                     hidden = c(20,2,20)
                                     )
# Anomaliebewertung
h2o_test.anomalie <- h2o.anomaly(titanic_anomalie, h2o_test)
test.anomalie = as.data.frame(h2o_test.anomalie)
h2o_test.anomalie_predict <- h2o.predict(titanic_anomalie, h2o_test)
test.anomalie_predict = as.data.frame(h2o_test.anomalie_predict)

# Was ist in dem Datensatz
head(test.anomalie_predict)

test = test %>% mutate(index = row.names(test))
result = cbind(test, test.prediction, test.anomalie, test.anomalie_predict)
result <- result %>% mutate(correct = Survived==predict)

# einen belibigen Wert als Linie darstellen
anomalie_schwellwert = 0.09

ggplot( result, aes(x=reorder(index, Reconstruction.MSE), y=result$Reconstruction.MSE, color=result$correct  ) ) +
  geom_point() +
  geom_hline(yintercept = anomalie_schwellwert)

# Anomalien ausgeben
head(result %>% filter(Reconstruction.MSE > anomalie_schwellwert))

# keine Anomalien ausgeben
head(result %>% filter(Reconstruction.MSE < anomalie_schwellwert))

# Die Modelle finden sich unter http://localhost:54321
