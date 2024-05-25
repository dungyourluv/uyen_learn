<?php 

namespace TodoApi\Model;

use DunnServer\Http\Request;
use DunnServer\Utils\DunnMap;
use TodoApi\Database\UserDb;

class UserModel {
    public $name;
    public $id;
    public $username;
    public $password;
    public $isAdmin = false;

    function save() {
        $db = new UserDb();
        if($db->has('id', $this->id)) {
            $db->update($this);
        }else {
            $db->insert($this);
        }
    }

    function delete() {
        $db = new UserDb();
        $db->delete($this->id);
    }

    function getWithId() {
        $db = new UserDb();
        $userModel = $db->find(UserModel::class, $this->id);
        return $userModel;
    }

    function getAll() {
        $db = new UserDb();
        $userModels = $db->findAll(UserModel::class);
        return $userModels;
    }

    function toResponse() {
        $userResponse = new UserResponse();
        $userResponse->name = $this->name;
        $userResponse->id = $this->id;
        $userResponse->username = $this->username;
        $userResponse->isAdmin = $this->isAdmin;
        return $userResponse;
    }

    /**
     * @throws \Exception
     * @return UserModel
     */
    function login() {
        $db = new UserDb();
        $users = $db->findAll(UserModel::class);
        for($i = 0; $i < count($users); $i++) {
            $user = $users[$i];
            if($user->username == $this->username && $user->password == $this->password) {
                return $user;
            }
        }

        throw new ExceptionModel("Invalid username or password", 400);
    }

    function register() {
        $db = new UserDb();
        if($db->has('username', $this->username)) {
            throw new ExceptionModel("Username is exists!", 400);
        }

        $this->save();
        return $this;
    }

    /**
     * @param Request $req
     */
    static function fromRequest($req) {
        $username = $req->getBody('username');
        $password = $req->getBody("password");
        $name = $req->getBody("name");
        $user = new UserModel();
        $user->username = $username;
        $user->password = $password;
        $user->name = $name;
        $user->id = uniqid();
        $user->isAdmin = false;

        $errors = $user->validate();
        if($errors->length() > 0) {
            throw new ExceptionModel(json_encode($errors), 400);
        }

        return $user;

    }

    function validate() {
        $errors = new DunnMap();
        if(!$this->username) {
            $errors->set('username', 'Username is required');
        }
        if(!$this->password) {
            $errors->set('password', 'Password is required');
        }

        if(strlen($this->password) < 6) {
            $errors->set('password', 'Password must be at least 6 characters');
        }

        if(!$this->name) {
            $errors->set('name', 'Name is required');
        }

        return $errors;
    }
}