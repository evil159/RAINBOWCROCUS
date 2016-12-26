var presentation = document.getElementById('presentation'),
        btn = $('.sidebar-show'),
        sideBar = $('.cabinet-sidebar'),
        sidebarShow = $('.sidebar-show-animation'),
        firstline = $('.sidebar-show-animation .firstline'),
        thirdline = $('.sidebar-show-animation .thirdline'),
        camera = $('#takePic'),
        sideBarIsShown = false,
        cameraIsShown = false,
        degrees = 0,
        degreesFirstLine = 0,
        degreesThirdLine = 0,
        looper, looperFirstLne, looperThirdLine, cw;

        $('.cabinet-sidebar .cabinet-menu li a').on('click', function(e)  {
            var clickedAttrLink = $(this).attr('href');
            $('.cabinet-container ' + clickedAttrLink).show().siblings().hide();
            $(this).parent('li').addClass('active').siblings().removeClass('active');
            showSidebar();
            e.preventDefault();
        });

        $('.cabinet-sidebar .cabinet-menu li a#search').on('click', function(e)  {

        });

        $('.name-container').on('click', function(e) {
            $('.cabinet-container #edit-profile').show().siblings().hide();
            $('.cabinet-container #edit-profile').parent('li').siblings().removeClass('active');
            e.preventDefault();
        });

        $('.btn-edit-profile').on('click', function(e) {
            $('.cabinet-container #edit-profile').show().siblings().hide();
            $('.cabinet-container #edit-profile').parent('li').siblings().removeClass('active');
            e.preventDefault();
        });

        $('#logout').on('click', function(e) {
            document.location.href = "login.html";
        });

        $('.btn-take-picture').on('click', function(e) {
            showCamera();
        });


        btn.click('click', function() {
            showSidebar();
        });

        function rotateAnimation() {
            degrees -= 3;

            sidebarShow.css({
                'transform' : 'rotate(' + degrees + 'deg)'
            });

            if (degrees == -180) {
                clearInterval(looper);
            }
        }

        function revertRotateAnimation() {
            degrees += 3;

            sidebarShow.css({
                'transform' : 'rotate(' + degrees + 'deg)'
            });

            if (degrees == 0) {
                clearInterval(looper);
            }
        }

        function rotateAnimationFirstLine() {
            degreesFirstLine -= 3;

            firstline.css({
                'transform' : 'rotate(-45deg) translate(-5px, -3px)'
            });

            if (degreesFirstLine == -45) {
                clearInterval(looperFirstLine);
            }
        }

        function revertRotateAnimationFirstLine() {
            degreesFirstLine += 3;

            firstline.css({
                'transform' : 'rotate(-0deg) translate(0px, 0px)'
            });

            if (degreesFirstLine == -0) {
                clearInterval(looperFirstLine);
            }
        }

        function rotateAnimationThirdLine() {
            degreesThirdLine += 3;

            thirdline.css({
                'transform' : 'rotate(45deg) translate(-5px, 3px)'
            });

            if (degreesThirdLine == 45) {
                clearInterval(looperThirdLine);
            }
        }

        function revertRotateAnimationThirdLine() {
            degreesThirdLine -= 3;

            thirdline.css({
                'transform' : 'rotate(0deg) translate(0px, 0px)'
            });

            if (degreesThirdLine == 0) {
                clearInterval(looperThirdLine);
            }
        }


        function showSidebar() {
            if (presentation.style.opacity == 0 && presentation.style.visibility == "hidden") {
                sideBarIsShown = true;
                presentation.style.opacity = 1;
                presentation.style.visibility = "visible";
                sideBar.css({
                    'transform' : 'translateX(0%)'
                });
            } else {
                presentation.style.opacity = 0;
                presentation.style.visibility = "hidden";
                sideBar.css({
                    'transform' : 'translateX(100%)'
                });
                sideBarIsShown = false;
            }

            if (degrees == 0) {
                looper = setInterval(rotateAnimation, 5);
                looperFirstLine = setInterval(rotateAnimationFirstLine, 5);
                looperThirdLine = setInterval(rotateAnimationThirdLine, 5);
            } else {
                looper = setInterval(revertRotateAnimation, 5);
                looperFirstLine = setInterval(revertRotateAnimationFirstLine, 5);
                looperThirdLine = setInterval(revertRotateAnimationThirdLine, 5);
            }
        }

        function showCamera() {
            if (!cameraIsShown) {
                camera.css({
                    'transform' : 'translateY(0%)'
                });
                $('.navbar').css('visibility', 'hidden');
                cameraIsShown = true;
            } else {
                camera.css({
                    'transform' : 'translateY(-100%)'
                });   
                $('.navbar').css('visibility', 'visible');
                cameraIsShown = false; 
            }
        }

        function showSearch() {
            
        }

//SWIPING RECOGNITION

$(function(){
  $('.mp-cabinet').on("swipe",function(){
    showSidebar();
  });        
  $('#presentation').on('swipe', function() {
    if(sideBarIsShown) {
        showSidebar();
    }
  }); 
  $('#takePic').on("swipe",function(){
    showCamera();
  });   
});

// $(function() {      
//   $( window ).swipe( {
//     swipeLeft:function(event, direction, distance, duration, fingerCount) {
//       showSidebar();
//     },
//     threshold:0
//   });
//   $("#presentation").swipe({
//     swipeRight:function(event, direction, distance, duration, fingerCount) {
//         if (sideBarIsShown) {
//             showSidebar();
//         }
//     },
//     threshold:0
//   });
// });

