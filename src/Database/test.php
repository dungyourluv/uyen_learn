<?php

use TodoApi\Database\JsonDatabase;

require_once 'vendor/autoload.php';

$db = new JsonDatabase("data.json");

class Job {
    public $name;
    public $id;
}

class User {
    public $name;
    public $id;
    public $job;
}

$user = new User();
$user->name = "John Doe";
$user->id = 1;
$user->job = new Job();
$user->job->name = "Developer";
$user->job->id = 1;

$db->insert($user);

$users = $db->findAll(User::class);

print_r($users);
