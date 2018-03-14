package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class UserTest {

    private Answer answer;
    private Board board;
    private User alice;
    private User bob;
    private Question question;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        board = new Board("Java");

        alice = board.createUser("alice");
        bob = board.createUser("bob");

        question = alice.askQuestion("What is a String?");
        answer = bob.answerQuestion(question, "It is a series of characters, strung together...");
    }

    @Test
    public void questionerReputationWhenUpvote() throws Exception {
        bob.upVote(question);

        assertEquals(5, alice.getReputation());
    }

    @Test
    public void answererReputationWhenUpvote() throws Exception {
        alice.upVote(answer);

        assertEquals(10, bob.getReputation());
    }

    @Test
    public void answererReputationWhenAnswerAccepted() throws Exception {
        alice.acceptAnswer(answer);

        assertEquals(15, bob.getReputation());
    }

    // extra reputation test that shows that downvoting an answer costs 1 reputation point

    @Test
    public void answererReputationWhenDownvote() throws Exception {
        alice.downVote(answer);

        assertEquals(-1, bob.getReputation());
    }

    @Test(expected = VotingException.class)
    public void upvotingNotAllowedByQuestionAuthor() throws Exception {
        alice.upVote(question);
    }

    @Test(expected = VotingException.class)
    public void downvotingNotAllowedByQuestionAuthor() throws Exception {
        alice.downVote(question);
    }

    @Test(expected = VotingException.class)
    public void upvotingNotAllowedByAnswerAuthor() throws Exception {
        bob.upVote(answer);
    }

    @Test(expected = VotingException.class)
    public void downvotingNotAllowedByAnswerAuthor() throws Exception {
        bob.downVote(answer);
    }

    @Test
    public void onlyQuestionerCanAcceptAnswer() throws Exception {
        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage("Only alice can accept this answer as it is their question");

        bob.acceptAnswer(answer);
    }
}