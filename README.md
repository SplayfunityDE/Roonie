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
> [!Note]
> Both methods require admin privileges on this repository!

Before going on make sure to check [if docker is installed on your system](https://www.docker.com/blog/how-to-check-docker-version/) because it is required for all further steps.
It is possible to provide the default Roonie service with and without a automatic workflow synchronization.
The only key difference in these types of provisioning are the way versions get updated. Without a workflow sync the container service has to be updated manually from the container registry using `docker pull`. It also requires the container service to be stopped during an update.

In both steps you first need to provide a `.env` File to give the service access to all credentials.
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

You also have to make sure to authenticate yourself on ghcr because Roonie's visibility is set to private by default.
```bash
echo <TOKEN> | docker login ghcr.io -u <GITHUB_USERNAME> --password-stdin
```
- `TOKEN` - [Personal Access Token](https://docs.github.com/de/enterprise-cloud@latest/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens) des berechtigten Github Accounts
- `GITHUB_USERNAME` - Benutzername des berechtiten Github Accounts

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
Replace `/DATA` with a configuration folder where the service saves some information using `.yaml` files and also replace `/PATH/TO/ENV_FILE` with the location of the actual .env file, you created previosly.
You can now simply start Roonie using the `docker compose up -d` command.

### With worklow sync
To start off, you need to create the following directory on your host system:
```bash
/opt/dockerfiles/roonie
```
Make sure to save all the files below in this folder!
Your `/opt/dockerfiles/roonie/docker-compose.yaml` should look like this:
```yaml
services:
  roonie:
    image: ghcr.io/splayfunityde/roonie:latest
    restart: always
    volumes:
      - /opt/dockerfiles/roonie:/bot
    env_file: .env
```
In the last step you need to provide the `HOST`, `USERNAME`, `PORT` and `SSH_PRIVATE_KEY` in the repository secret. Now the pipeline publishes directly to the configured host!

