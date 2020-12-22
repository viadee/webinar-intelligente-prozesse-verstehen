
# WICHTIG: Bei Erstinstallation zuerst install_everything.r _ausführen

source('libs.r')
source('import.r')

#features <- c('Age', 'Sex', 'Fare', 'Pclass', 'SibSp', 'Parch', 'Embarked', 'Cabin', 'CabinClass', 'peopleInCabin')
features <- c('TypeClass', 'Year', 'Doors', 'Passengers', 'Costs', 'RepairType')
labels <- 'Rejected'

#Traningsdaten
trainCsv <- claimCsvImport('../data/claimdata.csv')
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

# Automatisiertes Machinelles Lernen: Modell selbst lernen lassen
claim_aml = h2o.automl(x=features,
           y=labels,
           project_name='claim_example',
           training_frame = h2o_train,
           max_models = 25,
           nfolds = 5)

lb <-  h2o.get_leaderboard(claim_aml, extra_columns = 'ALL')
print(lb, n = nrow(lb)) 

claim_leader = claim_aml@leader
claim_leader@model_id

# Analyse des tranierten random Forest
h2o.confusionMatrix(claim_leader)
h2o.auc(claim_leader)

# Vergleiche den Einfluss der Variablen der Blackbox?
h2o.varimp_plot(claim_leader)

h2o_test.prediction = h2o.predict(claim_leader, h2o_test)
test.prediction = as.data.frame(h2o_test.prediction)

#test.results = merge(test, test.prediction, by.x=row.names(test), by.y=row.names(test.prediction))
test.results = cbind(test, test.prediction)

# Analyse des besten Modells
h2o.confusionMatrix(claim_leader, h2o_test)



# Anomalieerkennung
# einen Autoencoder tranieren
claim_anomalie <- h2o.deeplearning(x=features,
                                     seed=12345,
                                     model_id = "claim_autoencoder",
                                     training_frame = h2o_train,
                                     autoencoder = T,
                                     hidden = c(20,2,20)
                                     )
# Anomaliebewertung
h2o_test.anomalie <- h2o.anomaly(claim_anomalie, h2o_test)
test.anomalie = as.data.frame(h2o_test.anomalie)
h2o_test.anomalie_predict <- h2o.predict(claim_anomalie, h2o_test)
test.anomalie_predict = as.data.frame(h2o_test.anomalie_predict)

# Was ist in dem Datensatz
head(test.anomalie_predict)

test = test %>% mutate(index = row.names(test))
result = cbind(test, test.prediction, test.anomalie, test.anomalie_predict)
result <- result %>% mutate(correct = Rejected==predict)

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
