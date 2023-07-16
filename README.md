# my_task_front

## Description

Ce projet est le frontend de l'application Trello-like My Task. Il a été développé en utilisant JavaFX.

## Auteurs

- James ABIB
- Ronan KIELT
- Charles CRETOIS

## Prérequis

Assurez-vous d'avoir Docker et Docker Compose installés sur votre machine.

- [Docker](https://docs.docker.com/get-docker/)
- [Java17](https://www.oracle.com/java/technologies/downloads/#java17)

Il faut avoir le backend de l'application qui tourne sur votre machine. Pour cela, veuillez suivre les instructions du README.md du projet [my-task-back](https://github.com/GreenItForward/my_task_back).

## Installation

1. Clonez ce dépôt sur votre machine.

```sh
git clone https://github.com/votre-utilisateur/my-task-front.git
```

2.Accédez au répertoire du projet.
```sh
cd my-task-front
```

3. Lancez les conteneurs Docker à l'aide de Docker Compose.
   
```sh
docker-compose up -d
```

## Compilation et exécution
Assurez-vous que Docker et Docker Compose sont en cours d'exécution.

1. Dans un nouveau terminal, accédez au répertoire du projet.

```sh
cd my-task-front
```

2. Construisez le projet à l'aide de Maven.
```sh
mvn clean package
```

3. Lancez l'application à l'aide de Maven.
```sh
mvn javafx:run
```