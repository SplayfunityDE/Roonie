# Roonie

<img align="right" src="https://avatars.githubusercontent.com/u/108355696?s=200&v=4" height="200" width="200">

> [!Note]
> Sei vorsichtig! Alle Änderungen aus `master` werden direkt per Software-Deployment auf das Produktivsystem verteilt!

Bei Roonie handelt es sich um das Botsystem des [SPLAYFUNITY](https://discord.gg/V2Vc5hpRkH) Discord Servers.

## Features
Folgende Features sind in dem Botsystem enthalten:
- Support-Ticketsystem
- Temporäre Sprachkanäle
- Automatische Response-Verwaltung (Custom Commands)
- Umfragen
- Gewinnspiele (Giveaways)
- Minigames
- Server-Vorlagen (Bibliothek)
- Banner-Vorlagen (Bibliothek)
- Wirtschaftssystem (Economy)
- Integrierte Booster-Vorteile

## Backend
**Datenbank**

Die Speicherung jeglicher statischen Daten erfolgt über eine MongoDB Datenbank, welche Daten auf Basis von JSON-ähnlichen Textblöcken speichert.

**Deployment**

Die automatische Anwenungsverteilung läuft mittels [Github Actions](https://docs.github.com/de/actions) und wird in dem Workflow anschließend mit dem Dienst [rsync](https://wiki.ubuntuusers.de/rsync/) auf die zuständigen Linux Server verteilt. Dort werden Diese anschließend über einen systemd Service zu [Docker Images](https://docs.docker.com/engine/reference/commandline/image_ls/) und daraufhin zu [Docker-Containern](https://www.docker.com/resources/what-container/) umgewandelt und gestartet.
