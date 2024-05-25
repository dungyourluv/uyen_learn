<?php 

namespace TodoApi\Controller;

use DunnServer\MVC\Controller;
use TodoApi\Model\UserModel;

class LoginCtl extends Controller
 {
    function doPost($req, $res)
    {
        $username = $req->getBody("username");
        $password = $req->getBody("password");
        $user = new UserModel();
        $user->username = $username;
        $user->password = $password;
        try {
            $authUser = $user->login();
            $res->send($authUser->toResponse());
        } catch (\Throwable $th) {
            $res->status($th->getCode())->send($th);
        }
    }
 }