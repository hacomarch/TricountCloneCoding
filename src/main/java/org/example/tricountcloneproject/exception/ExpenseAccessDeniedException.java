package org.example.tricountcloneproject.exception;

public class ExpenseAccessDeniedException extends RuntimeException {
    public ExpenseAccessDeniedException() {
        super("Only participants involved in the settlement can view expenses.");
    }
}
