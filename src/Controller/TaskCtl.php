<?php 

namespace TodoApi\Controller;

use DunnServer\MVC\Controller;
use TodoApi\Model\TaskModel;

class TaskCtl extends Controller {
    function doGet($req, $res)
    {
        $model = new TaskModel();

        $tasks = $model->getAll();

        $tasks = array_map(function($task) {
            return $task->toResponse();
        }, $tasks);

        $res->send($tasks);
    }
}