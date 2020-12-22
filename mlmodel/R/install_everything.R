# Installieren neuer Pakete -----------------------------------------------
# Ausführen der einzelen Zeilen, oder des Gesamtblocks per STRG+ENTER
# Folgende Pakete sind notwendig für die Schulung:
install.packages("dplyr")
install.packages("ggplot2")
install.packages("h2o")
install.packages("MLmetrics")
install.packages("randomForest")
install.packages("mice")
install.packages("Hmisc")
install.packages("lime")
install.packages("tibble")
install.packages("tibbles")
install.packages("readr")
install.packages("tidyr")
install.packages("pacman")

# Verifizierung der Installation ------------------------------------------
library(pacman)
library(readr)
library(tidyr)
library(dplyr)
library(ggplot2)
library(h2o)
library(MLmetrics)
library(randomForest)
library(mice)
library(Hmisc)
library(lime)
library(tibble)

# Basisoperationen --------------------------------------------------------

# Variablenzuweisung
i <- 1
# Standardoperationen
i <- i+1
i <- i-1
i <- i*5
i <- i*10
print(i)
i

# IF-Else
if(i > 5){
  print(i)
} else {
  print("Sample")
}

# Schleifen
for (i in 1:10) {
  print(i)
}

# Wichtig, dieser Befehl muss laufen. Wenn Java installiert ist und das h2o package installiert wurde:
h2o.init()

# Lade das Iris Dataframe, das implizit in R enthalten ist
irisDataframe <- iris
irisDataframe
# Ausgabe einer Spalte

irisDataframe$Species




