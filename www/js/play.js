
var oui = document.getElementById("main_container").getElementsByClassName("square_div")[0].getElementsByClassName("elem_square");


oui[0].onclick = function() {playPvsAI(0)};
oui[0].onmouseover = function() {mouseOver(0)};
oui[0].onmouseout = function() {mouseOut(0)};

oui[1].onclick = function() {playPvsAI(1)};
oui[1].onmouseover = function() {mouseOver(1)};
oui[1].onmouseout = function() {mouseOut(1)};

oui[2].onclick = function() {playPvsAI(2)};
oui[2].onmouseover = function() {mouseOver(2)};
oui[2].onmouseout = function() {mouseOut(2)};

oui[3].onclick = function() {playPvsAI(3)};
oui[3].onmouseover = function() {mouseOver(3)};
oui[3].onmouseout = function() {mouseOut(3)};

function playPvsAI(i) {
    if(i==0){}
    location.replace("Player_vs_AI.html");//a changer

    if(i==1){
        
    }
    if(i==2){
        
    }
    if(i==3){
        
    }
}

function mouseOver(i) {
    if(i==0){
        document.querySelector("#main_container > div.square_div > div.top_left.elem_square > div.player-vs-ai").style.cursor = "pointer";
        document.querySelector("#main_container > div.square_div > div.top_left.elem_square > div.text_below_img > p").style.textShadow = "1px 1px 2px black";
        document.querySelector("#main_container > div.square_div > div.top_left.elem_square > div.text_below_img").style.backgroundColor = "#38a8b3";
    }
    if(i==1){
        document.querySelector("#main_container > div.square_div > div.top_right.elem_square > div.player-vs-player").style.cursor = "pointer";
        document.querySelector("#main_container > div.square_div > div.top_right.elem_square > div.text_below_img > p").style.textShadow = "1px 1px 2px black";
        document.querySelector("#main_container > div.square_div > div.top_right.elem_square > div.text_below_img").style.backgroundColor = "#ffc71a";
    }
    if(i==2){
        document.querySelector("#main_container > div.square_div > div.bottom_left.elem_square > div.ai-vs-ai").style.cursor = "pointer";
        document.querySelector("#main_container > div.square_div > div.bottom_left.elem_square > div.text_below_img > p").style.textShadow = "1px 1px 2px black";
        document.querySelector("#main_container > div.square_div > div.bottom_left.elem_square > div.text_below_img").style.backgroundColor = "#a069ba";
    }
    if(i==3){
        document.querySelector("#main_container > div.square_div > div.bottom_right.elem_square > div.free-mode").style.cursor = "pointer";
        document.querySelector("#main_container > div.square_div > div.bottom_right.elem_square > div.text_below_img > p").style.textShadow = "1px 1px 2px black";
        document.querySelector("#main_container > div.square_div > div.bottom_right.elem_square > div.text_below_img").style.backgroundColor = "#95c501";
    }
}

function mouseOut(i) {
    if(i==0){
        document.querySelector("#main_container > div.square_div > div.top_left.elem_square > div.player-vs-ai").style.cursor = "auto";
        document.querySelector("#main_container > div.square_div > div.top_left.elem_square > div.text_below_img > p").style.textShadow = "";
        document.querySelector("#main_container > div.square_div > div.top_left.elem_square > div.text_below_img").style.backgroundColor = "#694834";
    }
    if(i==1){
        document.querySelector("#main_container > div.square_div > div.top_right.elem_square > div.player-vs-player").style.cursor = "auto";
        document.querySelector("#main_container > div.square_div > div.top_right.elem_square > div.text_below_img > p").style.textShadow = "";
        document.querySelector("#main_container > div.square_div > div.top_right.elem_square > div.text_below_img").style.backgroundColor = "#694834";
    }
    if(i==2){
        document.querySelector("#main_container > div.square_div > div.bottom_left.elem_square > div.ai-vs-ai").style.cursor = "auto";
        document.querySelector("#main_container > div.square_div > div.bottom_left.elem_square > div.text_below_img > p").style.textShadow = "";
        document.querySelector("#main_container > div.square_div > div.bottom_left.elem_square > div.text_below_img").style.backgroundColor = "#694834";
    }
    if(i==3){
        document.querySelector("#main_container > div.square_div > div.bottom_right.elem_square > div.free-mode").style.cursor = "auto";
        document.querySelector("#main_container > div.square_div > div.bottom_right.elem_square > div.text_below_img > p").style.textShadow = "";
        document.querySelector("#main_container > div.square_div > div.bottom_right.elem_square > div.text_below_img").style.backgroundColor = "#694834";
    }


}
