anchors = document.querySelectorAll('.yu-anchor');
for (const anchor of anchors) {
    anchor.onclick = function(e) {
        window.location = this.dataset.yuHref;
    }
}

// function anime(index) {
//     let text_below_img = "";

//     switch (index) {
//         case 0:
//             text_below_img = document.querySelectorAll(".top_left > .text_below_img")[0];
//             text_below_img.style.backgroundColor = "#38a8b3";
//             text_below_img.style.textShadow = "1px 1px 2px black";
//             break;
//         case 1:
//             text_below_img = document.querySelectorAll(".top_right > .text_below_img")[0];
//             text_below_img.style.backgroundColor = "#ffc71a";
//             text_below_img.style.textShadow = "1px 1px 2px black";
//             break;
//         case 2:
//             text_below_img = document.querySelectorAll(".bottom_left > .text_below_img")[0];
//             text_below_img.style.backgroundColor = "#a069ba";
//             text_below_img.style.textShadow = "1px 1px 2px black";
//             break;
//         case 3:
//             text_below_img = document.querySelectorAll(".bottom_right > .text_below_img")[0];
//             text_below_img.style.backgroundColor = "#95c501";
//             text_below_img.style.textShadow = "1px 1px 2px black";
//             break;
//         default:
//             console.log('Error, we are out of ${index}.');
//     }
// }

// function deleteAnime(index) {
//     let text_below_img = "";

//     if (index === 0) {
//         text_below_img = document.querySelectorAll(".top_left > .text_below_img")[0];
//     }
//     else if (index === 1) {
//         text_below_img = document.querySelectorAll(".top_right > .text_below_img")[0];
//     }
//     else if (index === 2) {
//         text_below_img = document.querySelectorAll(".bottom_left > .text_below_img")[0];
//     }
//     else {
//         text_below_img = document.querySelectorAll(".bottom_right > .text_below_img")[0];
//     }

//     text_below_img.style.backgroundColor = "#694834";
//     text_below_img.style.textShadow = "";
// }

// function changeCursor(element) {
//     if (element.style.cursor === "") {
//         element.style.cursor = "pointer";
//     } 
//     else {
//         element.style.cursor = "";
//     }
// }

// function changePage(path) {
//     document.location.href = path;
// }

// /* TOP LEFT */
// var top_left = document.getElementsByClassName("top_left")[0];
// top_left.onmouseover = function() {
//     anime(0);
//     changeCursor(top_left);
// };
// top_left.onmouseout = function() {
//     deleteAnime(0);
//     changeCursor(top_left);
// };
// top_left.onclick = function() {
//     changePage("Player_vs_AI.html");
// };


// /* TOP RIGHT */
// var top_right = document.getElementsByClassName("top_right")[0];
// top_right.onmouseover = function() {
//     anime(1);
//     changeCursor(top_right);
// };
// top_right.onmouseout = function() {
//     deleteAnime(1);
//     changeCursor(top_right);
// };
// // top_right.onclick = function() {
// //     changePage("Player_vs_Player.html");
// // };


// /* BOTTOM LEFT */
// var bottom_left = document.getElementsByClassName("bottom_left")[0];
// bottom_left.onmouseover = function() {
//     anime(2);
//     changeCursor(bottom_left);
// };
// bottom_left.onmouseout = function() {
//     deleteAnime(2);
//     changeCursor(bottom_left);
// };
// bottom_left.onclick = function() {
//     changePage("AI_vs_AI.html");
// };


// /* BOTTOM RIGHT */
// var bottom_right = document.getElementsByClassName("bottom_right")[0];
// bottom_right.onmouseover = function() {
//     anime(3);
//     changeCursor(bottom_right);
// };
// bottom_right.onmouseout = function() {
//     deleteAnime(3);
//     changeCursor(bottom_right);
// };
// // bottom_right.onclick = function() {
// //     changePage("free_mode.html");
// // };