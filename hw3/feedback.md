### HW3 Feedback

**CSE 331 18sp**

**Name:** Jon Frederick Luntzel (luntzel)

**Graded By:** Leah Perlmutter (cse331-staff@cs.washington.edu)

### Score: 91/100
---

**Problem 3 - HolaWorld:** 5/5

- comment

**Problem 4 - RandomHello:** 10/10

- comment

**Problem 5 - Testing (Fibonacci) Java Code with JUnit :** 5/5

- comment

**Problem 6 - Answering Questions about the (Fibonacci) Code:** 10/15

- Question 1: 5/5
  - comment
- Question 2: 1/5
  - Your answer describes one of the changes that fixed testInductiveCase(). This question was about testBaseCase(). (-4)
- Question 3: 4/5
  - Half of the answer to this question was written as the answer to question 2 (-1)

**Problem 7 - Implementation:** 61/65

- Ball.java: 5/5
  - comment
- BallContainer.java: 25/25
  - comment
- Box.java: 31/35
  - Changed API of `BallContainer` by adding public method `getContentsList` to be called in `getBallsFromSmallest`. A better way would be to use the existing `BallContainer.iterator()` to iterate over the balls in it. (-2)
- Description of ways to implement GetBallsFromSmallest: Did not describe *how* you would deal with the duplicate case in TreeSet. Did not describe methods that add code in *different* parts of the `Box` class. A different way would be to use a private field in the Box that stores all the balls and gets sorted every time a ball is added. (-2)
