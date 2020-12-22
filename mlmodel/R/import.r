#library(dplyr)
#library(readr)
#library(tidyr)

titanicCsvImport <- function(filename) {
  data <- read_csv(filename, col_names = T, skip_empty_rows = T)
  if('Survived' %in% colnames(data)) {
    data <- data %>%
      #select(Survived, Age, Sex, Fare, Pclass, SibSp, Ticket, Parch, Cabin) %>%
      mutate( Survived = as.factor(Survived) )
  } else {
      #data <- data %>% 
      #  select(Age, Sex, Fare, Pclass, SibSp, Ticket, Parch, Cabin)
  }
  
  data <- data %>%
    replace(., is.na(.), "" ) %>% 
    #drop_na() %>%
    mutate( Age = as.numeric(Age) ) %>%
    mutate( Sex = as.factor(Sex) ) %>%
    mutate( Pclass = as.factor(Pclass) ) %>%
    mutate( Embarked = as.factor(Embarked) ) %>%
    mutate( Ticket = as.factor(Ticket) ) %>%
    mutate( Name = as.factor(Name) ) %>%
    mutate( CabinClass = substring(Cabin,1,1) ) %>%
    mutate( CabinClass = as.factor(CabinClass) ) %>%
    mutate( Cabin = as.factor(Cabin) ) %>%
    mutate( Fare = as.numeric(Fare) ) %>%
    mutate( hasTicket = Ticket != '') %>%
    mutate( knowsCabin = Cabin != '') %>%
    mutate( knowsAge = !is.na(Age) )
  
  cabins <- data %>%
    select(Cabin) %>%
    group_by(Cabin) %>%
    summarise( peopleInCabin = n() ) %>%
    arrange(desc(peopleInCabin))
  cabins[cabins$peopleInCabin > 50, ]$peopleInCabin <- 0
  data <- data %>% left_join(cabins, by = 'Cabin') 
  
  return(data)
}


claimCsvImport <- function(filename) {
  data <- read_csv2(filename, col_names = T, skip_empty_rows = T)
  if('Rejected' %in% colnames(data)) {
    data <- data %>%
      mutate( Rejected = as.factor(Rejected) )
  } else {
    #data <- data %>% 
    #  select(Age, Sex, Fare, Pclass, SibSp, Ticket, Parch, Cabin)
  }
  
  data <- data %>%
    replace(., is.na(.), "" ) %>% 
    #drop_na() %>%
    mutate( Year = as.numeric(Year) ) %>%
    mutate( TypeClass = as.factor(TypeClass) ) %>%
    mutate( Costs = as.numeric(Costs) ) %>%
    mutate( Passengers = as.numeric(Passengers) ) %>%
    mutate( Doors = as.numeric(Doors) ) %>%
    mutate( RepairType = as.factor(RepairType) )

  return(data)
}


