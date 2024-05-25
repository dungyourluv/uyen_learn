<?php 
namespace TodoApi\Database;

use Exception;
use ReflectionClass;
use ReflectionProperty;

class JsonDatabase
{
    private $filePath;

    public function __construct($filePath)
    {
        $this->filePath = $filePath;
        if (!file_exists($filePath)) {
            file_put_contents($filePath, json_encode([]));
        }
    }

    private function readData()
    {
        $data = json_decode(file_get_contents($this->filePath), true);
        if (json_last_error() !== JSON_ERROR_NONE) {
            throw new Exception("Invalid JSON data");
        }
        return $data;
    }

    private function writeData(array $data)
    {
        file_put_contents($this->filePath, json_encode($data, JSON_PRETTY_PRINT));
    }

    public function findAll($modelClass)
    {
        $data = $this->readData();
        return array_map(function ($item) use ($modelClass) {
            return $this->convertToModel($modelClass, $item);
        }, $data);
    }

    public function find($modelClass, $id)
    {
        $data = $this->readData();
        foreach ($data as $item) {
            if ($item['id'] == $id) {
                return $this->convertToModel($modelClass, $item);
            }
        }
        return null;
    }

    public function insert($model)
    {
        $data = $this->readData();
        $item = $this->convertToArray($model);
        $data[] = $item;
        $this->writeData($data);
    }

    public function update($model)
    {
        $data = $this->readData();
        $item = $this->convertToArray($model);
        foreach ($data as &$existingItem) {
            if ($existingItem['id'] == $item['id']) {
                $existingItem = $item;
                $this->writeData($data);
                return;
            }
        }
        throw new Exception("Item with ID {$item['id']} not found");
    }

    public function has($key, $value) {
        $data = $this->readData();
        foreach ($data as $item) {
            if ($item[$key] == $value) {
                return true;
            }    
        }
        return false;
    }

    public function delete($id)
    {
        $data = $this->readData();
        $data = array_filter($data, function ($item) use ($id) {
            return $item['id'] != $id;
        });
        $this->writeData(array_values($data));
    }

    private function convertToModel($modelClass, array $data)
    {
        $reflectionClass = new ReflectionClass($modelClass);
        $model = $reflectionClass->newInstanceWithoutConstructor();
        foreach ($data as $key => $value) {
            if ($reflectionClass->hasProperty($key)) {
                $property = $reflectionClass->getProperty($key);
                $property->setAccessible(true);
                $property->setValue($model, $value);
            }
        }
        return $model;
    }

    private function convertToArray($model)
    {
        $reflectionClass = new ReflectionClass($model);
        $properties = $reflectionClass->getProperties(ReflectionProperty::IS_PUBLIC | ReflectionProperty::IS_PROTECTED | ReflectionProperty::IS_PRIVATE);
        $data = [];
        foreach ($properties as $property) {
            $property->setAccessible(true);
            $data[$property->getName()] = $property->getValue($model);
        }
        return $data;
    }
}
