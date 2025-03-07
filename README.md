# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
## Sequence Diagram
[SequenceDiagram link](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0jYwA0MH246snQHOPz+qp9s30ow8BICKs7ewcAvpjCJTAFrOxclOX9g1BTqy9bC6pLUCsTH5tzR32h12QLObE43Fg1wuonKUCiMUyUAAFJForFKJEAI4+NRgACU52KoiuhVk8iUKnU5XsKDAAFUOsinlNCeTFMo1KpSUYdOUAGJITgwRmUdkwHRhFnAUaYdmUrlXaHElTlNA+BAIIkiFQ8+Wc6kwEDwuQoUUop7stnaBXqHnGcoKDgcEUddnaqgk676qmqcrGlCmhQ+MCpZHAEOpK1ym0G7nXB0wJ0u4Oh90w3W5a7gu5lCIIjFQSKqDVYHOQpWFC73GCPDovCZvOYR0P1CAAa3Qq0gnbQfTO1ah12y5nKACYnE5unWhjKxo25-8+i3Um3e92O12zugOKYvL5-AFoOxaTAADIQaJJAJpDJZZDmHmD8rVOpNVoGdQJNDTgb1xcLqMS4fF8PxrOo-YepQPLljWM7PABkyLu86ygasfxgrcFbKjqKDlAgl5CsiF5XtiuKxISGaGN6sa+jSdLmsy-6jNaFJxvafIwIKwrmu6PqKlmVYqnhMCWtoHpemStFcv6JqZKmYYrtG-F2gmnHJkmkbpsJlY3BCNYkUKxalpgsFDkJ1BwX+s5AYBMzNpGa5dhMPZblB5nwA+GDjpOv7SrZSFAVsK5OX2Lmbn226cHu3h+IEXgoOg56Xr4zA3ukmSYCOeQWSUL7SAAomeBX1AVzQtJ+qjft0IURe5MFYTWtW9qZjVQrlsIwAR9ipeGjkRZROk0WxdFGCg3DyZGfWtgNMYjQJhSJpEwwQDQmlpuJVG6WZ5QkalxkIGWbW6c+tYDsU0GCZ5ORgD5ThmNFnixYe8IumeiIwAA4nO3LpXeWVecwOF5ZUn0leV9hzjV-UtYODX6XmzXoK1CNPsJ+GIt9oyqNNq6zVtw0cqNtJgApuOhaxRMLbyApCimWniSp8bA51SmbUNUnzYaJNY2oyKU7a8aLZx72xF9P1zVTqks6qMCQ9jFSNBJmbZm1u2Yz9B1HajV2nf08tqOMFQuCbjTnZZHnZbdMATlOvR9AbGylMbptRbuT0HoE2A+FA2DcPAcmGLzKQZfeN1oxbL61A0ENQ8EMPoL+BsAHKIS85uXKrCPlEjaCzPrc6pwF6co7mEeerLAamrz5MRbMcCB7zZF4oNuF6tJ3N0mTucC+xam08KCl8R3zMdbLbPyMr1Gc1LfpGo3c7IgbykjxxjrOuLozaW3V07QHgaZE3agmWZJ0XXmBejAAktI4wAIxjgAzBnl3DoDd1J3ON-30-bsxZ7ARLDjQIskGAAApCAQpN6GACDoBAoB2wA3DsDGs1R6TvhaAbaGM1ey-j9sAIBUA4AQAIlAVYAB1FgV9SpLgNt-UoD9n71SzrmHOCc861nmPAwhxDSEUKoTQrYdDb4MN-qXbCY8RIACtIFoBrrnWYEChRHxxC3Ke7cuZzxJt3dhvdfRry4nTdaUZGar11ujGAE9gDqMJoLeiYAa7CL0dTRM3EXS82Hpos+uEaRzm3hXaeMgR7lD8FoQ+i9l7aGcapYW5R6TYDCUHPx7Md4sMhOUJRci5xa3Ea-XKcEX6W3fjbScD13b7jigELwBDPKBlgMAbAftCDxESCHf6Vty6oMKsVUq5VjDMMKHvEA3A8D81ye1IoPijQjJRK3AJGjZ7+hmWMpmBjEB1KTJqIJk8CZpJrOsvAOTT7mMjmdZhb8bp3TKUAA)

```mermaid
sequenceDiagram
    actor Client
    participant Server
    participant Handler
    participant Service
    participant DataAccess
    database db

    entryspacing 0.9
    group #navy Registration #white
    Client -> Server: [POST] /user\n{"username":"name", "password":"pass", "email":"email"}
    Server -> Handler: {"username":"name", "password":"pass", "email":"email"}
    Handler -> Service: register(RegisterRequest)
    Service -> DataAccess: getUser(username)
    DataAccess -> db: Find UserData by username
    DataAccess --> Service: null
    Service -> DataAccess: createUser(userData)
    DataAccess -> db: Add UserData
    Service -> DataAccess: createAuth(authData)
    DataAccess -> db: Add AuthData
    Service --> Handler: RegisterResult
    Handler --> Server: {"username":"name", "authToken":"token"}
    Server --> Client: 200\n{"username":"name", "authToken":"token"}
    end
    
    group #orange Login #white
    Client -> Server: [POST] /session\n{"username":"name", "password":"pass"}
    Server -> Handler: {"username":"name", "password":"pass"}
    Handler -> Service: login(LoginRequest)
    Service -> DataAccess: getUser(username)
    DataAccess -> db: Find UserData
    DataAccess --> Service: userData
    Service -> DataAccess: createAuth(authData)
    DataAccess -> db: Add AuthData
    Service --> Handler: LoginResult
    Handler --> Server: {"username":"name", "authToken":"token"}
    Server --> Client: 200\n{"username":"name", "authToken":"token"}
    end
    
    group #green Logout #white
    Client -> Server: [DELETE] /session\nauthToken
    Server -> Handler: authToken
    Handler -> Service: logout(authToken)
    Service -> DataAccess: getAuth(authToken)
    DataAccess -> db: Find AuthData
    DataAccess --> Service: authData
    Service -> DataAccess: deleteAuth(authToken)
    DataAccess -> db: Remove AuthData
    Service --> Handler: LogoutResult
    Handler --> Server: {}
    Server --> Client: 200
    end
    
    group #red List Games #white
    Client -> Server: [GET] /game\nauthToken
    Server -> Handler: authToken
    Handler -> Service: listGames(authToken)
    Service -> DataAccess: getAuth(authToken)
    DataAccess -> db: Find AuthData
    DataAccess --> Service: authData
    Service -> DataAccess: getGames()
    DataAccess -> db: List Games
    DataAccess --> Service: games[]
    Service --> Handler: ListGamesResult
    Handler --> Server: {"games":[...]}
    Server --> Client: 200\n{"games":[...]}
    end
    
    group #purple Create Game #white
    Client -> Server: [POST] /game\nauthToken\n{"gameName":"name"}
    Server -> Handler: authToken, {"gameName":"name"}
    Handler -> Service: createGame(authToken, CreateGameRequest)
    Service -> DataAccess: getAuth(authToken)
    DataAccess -> db: Find AuthData
    DataAccess --> Service: authData
    Service -> DataAccess: createGame(gameData)
    DataAccess -> db: Add GameData
    DataAccess --> Service: gameID
    Service --> Handler: CreateGameResult
    Handler --> Server: {"gameID":123}
    Server --> Client: 200\n{"gameID":123}
    end
    
    group #yellow Join Game #black
    Client -> Server: [PUT] /game\nauthToken\n{"playerColor":"WHITE", "gameID":123}
    Server -> Handler: authToken, {"playerColor":"WHITE", "gameID":123}
    Handler -> Service: joinGame(authToken, JoinGameRequest)
    Service -> DataAccess: getAuth(authToken)
    DataAccess -> db: Find AuthData
    DataAccess --> Service: authData
    Service -> DataAccess: getGame(gameID)
    DataAccess -> db: Find GameData
    DataAccess --> Service: gameData
    Service -> DataAccess: updateGame(gameData)
    DataAccess -> db: Update GameData
    Service --> Handler: JoinGameResult
    Handler --> Server: {}
    Server --> Client: 200
    end
    
    group #gray Clear application #white
    Client -> Server: [DELETE] /db
    Server -> Handler: clear()
    Handler -> Service: clear()
    Service -> DataAccess: clear()
    DataAccess -> db: Clear All Data
    Service --> Handler: ClearResult
    Handler --> Server: {}
    Server --> Client: 200
    end
```\


