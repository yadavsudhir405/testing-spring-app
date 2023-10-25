-- If Book table does not exist then create it
IF NOT EXISTS (
    SELECT 1
    FROM sys.databases
    WHERE name='Book'
)
BEGIN
    CREATE TABLE Book (
        Id INT IDENTITY(1,1) PRIMARY KEY,
        Title NVARCHAR(100) NOT NULL,
        Author NVARCHAR(100) NOT NULL,
        Price DECIMAL(18,2) NOT NULL
    )

END
