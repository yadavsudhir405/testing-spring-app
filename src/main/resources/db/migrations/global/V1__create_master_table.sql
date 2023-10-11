CREATE TABLE [dbo].[DB_Loc](
                               [DB_Loc_ID] [int] IDENTITY(1,1) NOT NULL,
                               [DB_Name] [nvarchar](150) NOT NULL,
                               [Server_Name] [nvarchar](150) NOT NULL,
                               [Server_Inst] [nvarchar](150) NULL,
                               [Port_Number] [int] NULL,
                               [Status_ID] [int] NOT NULL,
                               [DB_Type_ID] [int] NOT NULL,
                               [CreateDate] [smalldatetime] NOT NULL
                                   CONSTRAINT [PK_DB_Loc] PRIMARY KEY CLUSTERED
                                       (
                                        [DB_Loc_ID] ASC
                                           )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[DB_Loc] ADD  CONSTRAINT [DF_DatabaseLocation_CreateDate]  DEFAULT (getdate()) FOR [CreateDate]
GO
