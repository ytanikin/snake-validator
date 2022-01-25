<h1 align="center">Snake Validator</h1>
The original file is located at the end of this file.

## Usage
## Requirements

For building and running the application you need:

     Java 11 or higher

## Running the application locally

There are several ways to run the application on a local machine.

1. Execute the `main` method in the `com.yeldos.snakevalidator.SnakeValidatorApplication.kt` class from your IDE.
2. Execute `./mvnw spring-boot:run`.
3. Execute `mvn spring-boot:run`. Requires maven installed locally.


## Testing the application locally
There are two endpoints 
1. `GET` `/new?w=[width]&h=[height]`, creates a new game with the specified width and height.
2. `POST` `/validate`, validates snake eating the food.

Run end-to-end tests `infrustructure.controller.SnakeInputValidatorControllerTest`

## Description

The application creates and validates the snake eating the food without saving the 
state of the game, simply by validating the snake's movements.
the input JSON format for validating, is:

<details>
  <summary>Click to expand!</summary>

```
      {
        "gameId": "game id",
        "width": 5,
        "height": 5,
        "score": 0,
        "fruit": {
          "x": 4,
          "y": 4
        },
        "snake": {
          "x": 0,
          "y": 0,
          "velX": 1,
          "velY": 0
        },
        "ticks": [
          {
            "velX": 1,
            "velY": 0
          },
          {
            "velX": 1,
            "velY": 0
          },
          {
            "velX": 0,
            "velY": 1
          }
        ]
      }
```
</details>

The response is different for valid and invalid requests.


####Response:
- *200*: Valid state & ticks. Returns JSON marshaled `state` with new
randomly generated fruit position and a score incremented by 1.
- *400*: Invalid request.
- *404*: Fruit not found, the ticks do not lead the snake to the fruit
position.
- *405*: Invalid method.
- *418*: Game is over, snake went out of bounds or made an invalid move.
- *500*: Internal server error.


## Structure

The code is divided into three parts:
1. `infrastructure` - currently contains only controller, but devoted as well
   for persistence.
2. `service` - the intermediate layer between infrastructure and domain layer. Ensures that
   the data is valid to be processed by the domain layer.
3. `domain layer` -  business logic.


```
    ├── infrastructure        
    |     └── controller
    |         ├── SnakeValidatorController
    |         ├── request
    |         |   ├── StateRequest
    |         |   ├── SnakeRequest
    |         |   ├── FruitRequest
    |         |   └── TickRequest
    |         └── response
    |             └── ErrorResponse
    ├── service               
    |     ├── SnakeValidatorService
    |     ├── InputValidator
    |     ├── MaliciousService
    |     └── SnakeRequestMapper
    └── domain
        ├── Game
        └── Board
            ├── Snake 
            └── Fruit
       
```




## Architecture
A starting point for implementing `Hexagonal architecture`. You will also find it
named `Clean Architecture`, `Ports-And-Adapters`, or commonly `Onion Architecture`.

The basic idea is to make the domain layer independent of any library
and other layers such as service and infrastructure layers.
It is easily visible by looking at the import section, there are only Java SDK imports.
This can come in handy when you need to replace the repository layer (for instance, for scaling)
and remain the domain layer untouched.

1. An `infrastructure layer` receives the request and sends it to the service layer.
2. `Service layer`  acts as an adapter to the domain layer. It validates an input,
   in order to have only valid data before instantiating domain entities,
   maps the data to the `domain entities`.
3. `Domain layer` is the business logic. It is responsible for the game logic. There are only
   4 domain entities `Game`, `Board`, `Snake`, `Fruit`, and one value object `Coordinate`.
4. Domain layer is not coupled to any of the layers, If you need to add persistence, you can simply:
    1. Add repository interfaces to the domain layer,
    2. Implement the interface in the infrastructure layer and
    3. Use the interface in the service layer
    4. Fetch data from the database,
    5. Map database entities to domain entities and simply use the domain entities.
    6. Domain layer is remained untouched.

Separation of concerns is also a key part of the domain entities themselves,
they are autonomous and can be used separately.


#### Notes
* All the logic of the requirements is fit in the domain entities,
  hence having `Manager`, Handler, `Service`, `Aggregate` or `Repository`(interfaces)
  inside the domain layer is unnecessary.

* The domain entities are rich in logic, hence they are the only place
  where the business logic is implemented. No [Anemic Domain Model](https://martinfowler.com/bliki/AnemicDomainModel.html)
  are used, which is an anti-pattern.

* `Immutability` is a key part of the architecture. The domain entities are immutable, this 
brings the benefits of immutability such as: `Testability`, `Reusability`, `Debug-ability`
(for instance, you never had a doubt that `String` is altered or not. This gives a possibility comparing 
values during debugging like old and new data, as well as no side effects), `Reference Copying`, `Thread Safety`, `Maintainability`.
* The domain entities are not coupled with any library, hence they are easily testable.
* I used `TDD` approach. The first thing I did after designing the domain entities on the paper,
was to create `Integration Tests` for `controller` covering all the requirements and corner cases, 
which helped me during the development.
* Used technologies: `Spring Boot`, `Hexagonal Architecture`, `Factory Methods`,  `Immutabilty`,
`Kotlin features` (such as sealed classes, data classes with its powerful copy function,
sequence, collection range, zip, lazy initialization, etc.). 

### Preventing Malicious Manipulation

There are two ways to prevent malicious manipulation that come to my mind:
1. Use Cookie to store encrypted data with values of the game state(such as
fruit, snake positions and gameId) and validate the token is equal to the token
newly generated by the service taking into account the same game state and secret key.
2. Similar to the first approach, but the token is saved in the request itself.
3. Generate a fruit position based upon the game state and the secret key, so that there is only
a few possible positions for the fruit for the current state of the game (not reliable and
difficult to implement).
4. Obvious idea is to store the game state in persistent storage.

Implemented the second approach, but with an exception in order to pass existing tests provided by you. 
To turn the malicious validator off you can simply assign `false` to 
`service.impl.MaliciousServiceImpl.getMaliciousValidationEnabled`

The implementation is very simple and not secure(no cipher, aes, salt) in addition I simply copied for testing,
but it is enough for the requirements to show the idea.

### Drawbacks of the implementation

* Entry-level developers have to learn Hexagonal Architecture. But once they get idea of DDD,
Hexagonal architecture, and immutability they will quickly implement new features. 
For instance, if you want to add tail, all of you need to do is to create
new property in `Snake` and take into account the tail position during the `move` of `Snake`
and generating `Fruit`, that is very simple.
* It is not easy to meet your expectation regarding the simplicity of application. I could easily 
write the logic inside controller or service layer without small or no abstraction at all. But this is
not simple to extend and maintain, but probably will be simple to understand. Simple could mean as well no technology used, no dependency injection,
no `Framework` used. I have noticed you used `Go` to implement tests provided by you,
which is actually truly functional language
(there is no even exceptions, honestly, that I don't like yet, because I have lack of experience with it, 
and reading articles about that is not enough) therefore decided to use `Kotlin` to implement tests and 
immutability.
* About `Immutability`:
  * DDD is not a good fit with `Immutability`
  * `Immutability` is not a good fit with bidirectional dependencies
  * `Immutability` is not easy to design, I may be wrong, and we can get used to do it easily
  * Immutable data is not easy to extend to some extent, for example it will be hard to extend the 
  `Game` or `Board` with too many new interactions, like adding to the board too many objects or characters,
  I may be wrong, and it is ready for such a scenario.
  * `Immutability` is bad for performance. However, considering that nowadays implementing and maintaining 
  is more expensive than having fast servers this can be not a problem.
  * `Immutability` brings: `Memory Usage` (not a big difference, considering
      that at some point immutable data decreases memory usage, and Java garbage collector
      works very well with too many allocated instances in the memory)
* I don't like the Game has a logic when and what to copy itself, 
it would make sense to introduce, GameManager(Singleton) in domain layer that would act 
as a service and extract some logic from the Game.

