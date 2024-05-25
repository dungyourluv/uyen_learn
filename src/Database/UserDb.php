<?php 
namespace TodoApi\Database;

class UserDb extends JsonDatabase {
    function __construct()
    {
        parent::__construct(__DIR__ . "/users.json");
    }
}