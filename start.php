<?php

function isLinux() {
    return PHP_OS_FAMILY === 'Linux';
}

function isWindows() {
    return PHP_OS_FAMILY === 'Windows';
}

function checkVendorFolder() {
    return is_dir('vendor');
}

function isMysqlRunning() {
    if (isLinux()) {
        $output = shell_exec('service mysql status');
        return strpos($output, 'active (running)') !== false;
    } elseif (isWindows()) {
        $output = shell_exec('sc query mysql');
        return strpos($output, 'RUNNING') !== false;
    }
    return false;
}

function startMysql() {
    if (isLinux()) {
        $output = shell_exec('sudo service mysql start');
    } elseif (isWindows()) {
        $output = shell_exec('net start mysql');
    }
    return $output;
}

function getLocalIpAddress() {
    if (isLinux()) {
        $output = shell_exec('hostname -I');
        $ips = explode(' ', trim($output));
        return $ips[0];
    } elseif (isWindows()) {
        $output = shell_exec('ipconfig');
        preg_match('/IPv4 Address[\.\ ]*: ([0-9\.]+)/', $output, $matches);
        return $matches[1];
    }
    return '127.0.0.1';
}

function updateFiles($ip, $files) {
    $pattern = '/(?:[0-9]{1,3}\.){3}[0-9]{1,3}:8080|localhost:8080/';
    $replacement = $ip . ':8080';

    foreach ($files as $file) {
        if (file_exists($file)) {
            $content = file_get_contents($file);
            $updatedContent = preg_replace($pattern, $replacement, $content);
            file_put_contents($file, $updatedContent);
            echo "Updated $file\n";
        } else {
            echo "File $file does not exist\n";
        }
    }
}

function runComposerStart() {
    $output = shell_exec('composer start');
    return $output;
}

// Main script
if (!isLinux() && !isWindows()) {
    echo "This script is for Linux or Windows OS only\n";
    exit(1);
}

if (!checkVendorFolder()) {
    echo "Can't find vendor/ folder. Start install composer\n";
    shell_exec('composer install');
    exit(1);
}

if (!isMysqlRunning()) {
    echo "Mysql service is not running. Start mysql service\n";
    startMysql();
}

$ip = getLocalIpAddress();

$pathArray = ['./.vscode/launch.json', 'composer.json'];
updateFiles($ip, $pathArray);

echo runComposerStart();
?>
