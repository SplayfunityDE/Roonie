# Roonie

<img align="right" src="https://avatars.githubusercontent.com/u/108355696?s=200&v=4" height="200" width="200">

> [!Note]
> Changed made in `master` are directly synced with the remote host!

Roonie is the bot system of the [SPLAYFUNITY](https://splayfer.de) Discord server.

## Features
The following features are included in the bot system:
- Support Ticket System
- Temporary Voice Channels
- Automatic Response Management (Custom Commands)
- Polls
- Giveaways
- Minigames
- Server Templates (Library)
- Banner Templates (Library)
- Economy System
- Integrated Booster Benefits

## Backend
**Database**

This repository uses the default SPLAYFUNITY MongoDB database.


**Deployment**

Deployment is accessing varios open source ressources and interfaces.
- Github Actions & Docker Buildx
- Ghcr
- Gradle
- Docker Compose

## Setup 
It is possible to provide the default Roonie service with and without a automatic workflow synchronization.
The only key difference in these types of provisioning are the way versions get updated. Without a workflow sync the container service has to be updated manually from the container registry using `docker pull`. It also requires the container service to be stopped during an update.

First we need to provide a `.env` File to give the service access to all credentials.
It should contain the following values:
```env
MONGO_HOST=123.456.789.123
MONGO_USERNAME=user
MONGO_PASSWORD=password
BOT_TOKEN=someToken
MEDIA_PATH="/bot"
```
- `MONGO_HOST` - the ip address of the mongodb database
- `MONGO_USERNAME` - a database user with access to the "splayfunity" database. If the user doesn't have administrative privileges, you have to create this database by yourself and grant pernmissions for this user.
- `MONGO_PASSWORD` - password credentials for the mongodb user
- `BOT_TOKEN` - the discord bot's token from the official [Discord Developer Portal](https://discord.com/developers/applications)
- `MEDIA_PATH` - This field is only important if you plan to host your service accross multiple operating systems. By default it's value should be set to "/bot"

### Without workflow sync
To setup a the basic service without a sync to github actions you need to create a simple `docker-compose.yaml` to provide the environment for Roonie.
The file path for your compose file doesn't matter. You can used the docker-compose file provided in this repository.
```yaml
services:
  roonie:
    image: ghcr.io/splayfunityde/roonie:latest
    restart: always
    volumes:
      - /DATA:/bot
    env_file: /PATH/TO/ENV_FILE
```
Replace `/DATA` with a configuration folder where the service saves some information using `.yaml` files and also replace `/PATH/TO/ENV_FILE` with the location of the actual .env file, you created priviosly.

### With worklow sync
> [!Note]
> This method requires admin privileges on this repository!
To start off, you need to create the following directory on your host system:

