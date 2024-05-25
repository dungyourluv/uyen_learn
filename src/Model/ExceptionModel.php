<?php 

namespace TodoApi\Model;

class ExceptionModel extends \Exception implements \JsonSerializable {
    public $message;
    public $code;

    function __construct($message, $code) {
        $this->message = $message;
        $this->code = $code;
    }

    function jsonSerialize() {
        return [
            'message' => $this->message,
            'code' => $this->code
        ];
    }
}