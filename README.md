# Design Choices

*H2 Database
   * I have not had the opportunity to work with a H2 database yet, but since we don't need to persist the data, the
     embedded database offered ease of setup.
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
consideration, I'm very excited at the prospect of being a part of Parallon and getting the chance to work in the Java world again.
