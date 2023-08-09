## Running the application

The project is a standard Maven project. To run it from the command line, type `mvnw` (Windows), or `./mvnw` (Mac &
Linux), then open http://localhost:8080 in your browser.

# Design Choices

* Generic 500 errors
    * I'm of the opinion that we should be returning as little information back to the user as is necessary in cases
      where they may not be the owner of the bank account. This is why I have opted to swallow exceptions and respond
      with 500s and instead log the message and cause.
* Model Construction
    * I kept the models as simple as possible to achieve the stated goals, using a Customer's email as their unique
      identifier to prevent duplicate accounts.
    * The accountNumber is meant as a user-facing identifier for the account
* Transactions
    * Transactions are meant to have differing originating and destination accounts for transfers, but the same accounts
      for withdrawals/deposits. In a production environment I think I would prefer to flag the tx and classify them in a
      field on the model. This would make aggregation of transactions simpler as well.
    * I also considered creating a TransactionsController, but since Transactions are intrinsically tied to Accounts,
      I'd be pulling both repos into the services either way

Please do reach out with any other questions you may have about methodology/thought process. Thank you for your time and
consideration.