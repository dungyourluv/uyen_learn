<?php 

namespace TodoApi\Model;

use DunnServer\Http\Request;
use DunnServer\Utils\DunnArray;
use DunnServer\Utils\DunnMap;
use TodoApi\Database\TaskDb;
use TodoApi\Database\UserDb;

class TaskModel {
    public $id;
    public $title;
    public $description;
    public $status = 'pending';
    public $userId;
    public $images;

    function save() {
        $db = new TaskDb();
        if($db->has('id', $this->id)) {
            $db->update($this);
        }else {
            $db->insert($this);
        }
    }

    function delete() {
        $db = new TaskDb();
        $db->delete($this->id);
    }

    /**
     * @return TaskModel
     */
    function getWithId() {
        $db = new TaskDb();
        $taskModel = $db->find(TaskModel::class, $this->id);
        return $taskModel;
    }

    /**
     * @return TaskModel[]
     */
    function getAll() {
        $db = new TaskDb();
        $taskModels = $db->findAll(TaskModel::class);
        return $taskModels;
    }

    /**
     * @return TaskResponse
     */
    function toResponse() {
        $userDb = new UserDb();
        $taskResponse = new TaskResponse();
        $userModel = $userDb->find(UserModel::class, $this->userId);
        $taskResponse->id = $this->id;
        $taskResponse->title = $this->title;
        $taskResponse->description = $this->description;
        $taskResponse->status = $this->status;
        $taskResponse->user = $userModel->toResponse();
        $host = $_SERVER['HTTP_HOST'];
        $taskResponse->images = array_map(function ($img) use ($host) {
            return "http://$host$img";
        }, $this->images ?? []);
        return $taskResponse;
    }

    /**
     * @param Request $req
     */
    static function fromRequest($req) {
        /**
         * @var UserModel $user
         */
        $user = $req->getParams("user");
        $task = new TaskModel();
        $task->id = uniqid();
        $uploads = $req->getUploads();
        $images = $uploads->get("images", new DunnArray());
        $task->title = $req->getBody("title") ?? "";
        $task->description = $req->getBody("description") ?? "";
        $task->userId = $user->id;
        $task->images = $images->filter(function($image) {
            return preg_match("/^image\/*/", $image->getType());
        })->map(function($image) {
            return $image->getPath();
        })->toArray();

        $errors = $task->validate();
        if($errors->length() > 0) {
            throw new ExceptionModel(json_encode($errors), 400);
        }

        return $task;
    }

    function validate() {
        $attributes = new DunnMap(get_object_vars($this));
        $errors = new DunnMap();
        if(!$attributes->get("title")) $errors->set("title", "Title is required");
        if(!$attributes->get("description")) $errors->set("description", "Description is required");
        return $errors;
    }
}