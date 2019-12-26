-- CREATE DATABASE project3;
-- 
-- USE project3;


CREATE TABLE users(username nvarchar(255) NOT NULL
, displayname nvarchar(MAX) NOT NULL
, PRIMARY KEY CLUSTERED (username)
);

-- This is server user, insert this to users first else get conflict constraint
-- when insert message to MessageDetail table, Cause client send a message to server
-- then server save it to MessageDetail and there is no user with this from user
-- existed in users table, so conflict will be occcured
INSERT INTO users values('127.0.0.1', 'Server');

CREATE TABLE MessageDetail(MessageID int IDENTITY(1,1) NOT NULL 
, FromUser NVARCHAR(255) 
, ToUser NVARCHAR(255)
, DateCreated smalldatetime NOT NULL
, [Content] NVARCHAR(MAX)
, MessageType NVARCHAR(255) NOT NULL
, PRIMARY KEY CLUSTERED (MessageID) 
, FOREIGN KEY (FromUser) REFERENCES users(username) 
, FOREIGN KEY (ToUser) REFERENCES users(username)
);

-- drop table MessageDetail;
-- drop table users;
