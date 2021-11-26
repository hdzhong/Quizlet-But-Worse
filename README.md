# My Personal Project

## Interactive Flashcards

### What is this?
My project is an interactive flashcard system that can hold multiple sets of flashcards. Each flashcard set can have a 
category and store many flashcards. Each flashcard will be able to store a question and answer and can be marked as 
complete once the user is confident! 

Users will eventually be able to:
* play games with the flashcards
* randomize cards within the set
* create multiple choice questions
* test themselves with fill-in-the-blank exercises
* and more!

### Who is this for?
The flashcard system will be useful to any student studying multiple subjects at once.

### Why am I making this?
This project is interesting to me as I have always used flashcard tools when I studied before. I believe it would
be a very useful tool to create and tailor to my own needs.


## User Stories
### Phase 1 
- As a user, I want to be able to view all my flash card sets
- As a user, I want to be able to view each of the individual flash cards inside the sets
- As a user, I want to be able to add new flashcard sets to a library of flashcards
- As a user, I want to be able to add new flash cards to the individual sets
- As a user, I want to be able to delete individual flash cards and sets
- As a user, I want to be able to mark sets and individual cards as complete and see which ones are not completed


### Phase 2
- As a user, I want to be able to save my flashcard sets and individual cards
- As a user, when I open the app, I want the option to be able to load my previous sets
- ~~As a user, I want to be able to play a game where I try to correctly match the front of the card with the definition~~

### Phase 3
- As a user, I want to be able to add multiple flashcards to a flashcard set in a GUI
- As a user, I want to be able to add multiple flashcard sets to the library in a GUI
- As a user, I want to be able to visually see all my sets and flashcards
- As a user, I want to be able to load and save the state of the application

### Phase 4: Task 3
- I had to drop a lot of functionality when migrating from a console based UI to my graphical UI. 
- I hope to add back in some functionality such as the matching game and a more robust search function
- I hope to refactor the toolbar menu of my flashcard UI as a separate panel
- I hope to make my application more robust by addition my exceptions, try/catch clauses, and assertions
- I hope to implement a bidirectional relationships between all my models to prevent duplication of certain items
- Right now I have more than one UI element coupled to my model but I believe it is possible to reduce the coupling by 
implementing the observer design pattern instead

### Phase 4: Task 2
**Sample log:**

Sun Nov 21 16:31:09 PST 2021
Added Log Test to library

Sun Nov 21 16:31:09 PST 2021
Added Empty Card to Log Test

Sun Nov 21 16:31:16 PST 2021
Empty Card has been edited to Card1 front

Sun Nov 21 16:31:16 PST 2021
Please Edit has been edited to Card1 back

Sun Nov 21 16:31:22 PST 2021
Added Card2 front to Log Test

Sun Nov 21 16:31:29 PST 2021
Added Card3 front to Log Test

Sun Nov 21 16:31:33 PST 2021
Card has been flipped

Sun Nov 21 16:31:34 PST 2021
Card3 front is now complete!

Sun Nov 21 16:31:36 PST 2021
Removed Card3 front from Log Test

Sun Nov 21 16:31:39 PST 2021
Removed Log Test from library

Sun Nov 21 16:31:40 PST 2021
Exited application

