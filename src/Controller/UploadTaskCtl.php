<?php 

namespace TodoApi\Controller;

use DunnServer\MVC\Controller;
use TodoApi\Model\TaskModel;

class UploadTaskCtl extends Controller {
    function doPost($req, $res)
    {
        try {
            $task = TaskModel::fromRequest($req);
            $task->save();
            $res->send($task->toResponse());
        } catch (\Throwable $th) {
            $res->status($th->getCode())->send($th);
        }
    }
}