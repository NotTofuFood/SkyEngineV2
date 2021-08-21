var canvas = document.getElementById("canvas_id");
var ctx = canvas.getContext("2d");

var width;
var height;
var offset = 0;
var offset_height = 0;

function Draw() {
    ctx.fillStyle = "rgb(140,138,160)";
    ctx.fillRect(0,0,canvas.clientWidth, canvas.clientHeight);

    ctx.strokeStyle = "rgba(255,255,255,.4)";

    for(let x = 0; x < width+16; x+=16) {
        for(let y = 0; y < height+16; y+=16) {
            ctx.strokeRect(x,y,16,16);
        }
    }
}

function UpdateLoop() {
    Draw();
    window.requestAnimationFrame(UpdateLoop);
}

function Start() {
    canvas.width = document.getElementById("Editor").clientWidth-offset;
    canvas.height = document.getElementById("Editor").clientHeight-offset_height;
    width = document.getElementById("Editor").clientWidth-offset;
    height = document.getElementById("Editor").clientHeight-offset_height;
}

window.onresize = function() {
    canvas.width = document.getElementById("Editor").clientWidth-offset;
    canvas.height = document.getElementById("Editor").clientHeight-offset_height;
    width = document.getElementById("Editor").clientWidth-offset;
    height = document.getElementById("Editor").clientHeight-offset_height;
}

window.onload = function() {
    Start(); 
    UpdateLoop();
}