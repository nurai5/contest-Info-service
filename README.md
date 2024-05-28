# Contest Info Service

## Overview

Contest Info Service is a Spring Boot application designed to fetch and parse contest information from a specified [website](https://mkk.gov.kg/ru/) and send notifications to a Telegram channel using a Telegram Bot.

## Features

- Scheduled fetching and parsing of contest information from a specified [website](https://mkk.gov.kg/ru/).
- Storing contest information in a database.
- Sending contest information to a Telegram channel with formatted messages and inline buttons.

## Technologies Used

- Java 17
- Spring Boot
- Spring Scheduling
- Jsoup (for HTML parsing)
- Telegram Bots API
- REST Template
- SQLite
- Maven

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Telegram Bot API token
- Telegram Channel ID

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/nurai5/contest-Info-service.git
   cd contest-info-service

2. Create a Telegram bot and obtain the API token from BotFather.

3. Set up your application.properties file with the necessary configuration:
   ```
   spring.datasource.url=jdbc:sqlite:contestInfoService.db
   spring.datasource.driver-class-name=org.sqlite.JDBC
   spring.jpa.show-sql=true
   spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
   spring.jpa.hibernate.ddl-auto=update

   bot.name=YourBotName
   bot.token=YourBotToken
   bot.channelId=@YourChannelId
   ```
4. Build the project using Maven:
   ```sh
   mvn clean install

5. Run the application:
   ```sh
   mvn spring-boot:run

## Usage
The application is set up to fetch and parse contest information from the specified website on a scheduled basis (at 18:55 and 20:55 daily). The parsed information is stored in a database, and new contest details are sent to a Telegram channel.

## Scheduled Task
The ContestParsingService class is responsible for fetching and parsing HTML content from the specified URL. It uses Jsoup to parse the HTML and extract contest details. The parsed data is then saved to the database, and a message is sent to the Telegram channel.

## Sending Telegram Messages
The TelegramBot class handles sending messages to the Telegram channel. It formats the contest information and includes an inline button with a link to the contest details.

## Contributing
1. Fork the repository.
2. Create your feature branch (git checkout -b feature/YourFeature).
3. Commit your changes (git commit -am 'Add some feature').
4. Push to the branch (git push origin feature/YourFeature).
5. Create a new Pull Request.

## Acknowledgments
[Spring Boot](https://spring.io/projects/spring-boot)

[Hibernate](https://hibernate.org/)

[Jsoup ](https://jsoup.org/)

SQLite

Telegram Bots API

[Lombok](https://projectlombok.org/)

[Selenium](https://www.selenium.dev/)

[Maven](https://maven.apache.org/)

The creators of various Java libraries and tools used in this project.


