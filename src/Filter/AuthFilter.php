<?php 

namespace TodoApi\Filter;

use DunnServer\Middlewares\Filter;
use TodoApi\Model\ExceptionModel;
use TodoApi\Model\UserModel;

class AuthFilter implements Filter {
    function doFilter($req, $res, $chain)
    {
        try {
            $token = $req->getHeaders("Authorization");
            if(!$token) throw new ExceptionModel("Authorization Header is required", 401);
            $token = explode(" ", $token);
            if(count($token) != 2) throw new ExceptionModel("Invalid Authorization Header", 401);
            if($token[0] != "Basic") throw new ExceptionModel("Use Basic Auth", 401);
            $token = base64_decode($token[1]);
            $token = explode(":", $token);
            if(count($token) != 2) throw new ExceptionModel("Invalid Authorization Header", 401);
            $user = new UserModel();
            $user->username = $token[0];
            $user->password = $token[1];
            $user = $user->login();
            $req->setParams("user", $user);
            $chain->doFilter($req, $res);
        }catch(\Exception $e) {
            $res->status($e->getCode())->send($e);
        }

    }
}