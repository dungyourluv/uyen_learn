<?php 

namespace TodoApi\Database;

class TaskDb extends JsonDatabase {
    function __construct()
    {
        parent::__construct(__DIR__ . "/tasks.json");
    }
}