<?php 

namespace TodoApi\Filter;

use DunnServer\Middlewares\Filter;

class IsAdminFilter implements Filter {
    function doFilter($req, $res, $chain)
    {
        $user = $req->getParams("user");
        if(!$user->isAdmin) {
            $exception = new \TodoApi\Model\ExceptionModel("You are not authorized to access this resource", 403);
            return $res->status($exception->getCode())->send($exception);
        }
        $chain->doFilter($req, $res);
    }
}