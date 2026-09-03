// =========================
// ELEMENTS
// =========================

const stage =
    document.getElementById(
        "carouselStage"
    );


const cards =
    Array.from(
        document.querySelectorAll(
            ".demo-card"
        )
    );


const prevButton =
    document.getElementById(
        "prevButton"
    );


const nextButton =
    document.getElementById(
        "nextButton"
    );


const dotsContainer =
    document.getElementById(
        "dots"
    );


const currentNumber =
    document.getElementById(
        "currentNumber"
    );




// =========================
// STATE
// =========================

let currentIndex = 0;

let dragging = false;

let startX = 0;

let dragOffset = 0;




// =========================
// DOTS
// =========================

cards.forEach(
    (
        card,
        index
    ) => {

        const dot =
            document.createElement(
                "button"
            );


        dot.classList.add(
            "dot"
        );


        dot.addEventListener(
            "click",
            () => {

                currentIndex =
                    index;


                render();

            }
        );


        dotsContainer.appendChild(
            dot
        );

    }
);


const dots =
    Array.from(
        document.querySelectorAll(
            ".dot"
        )
    );




// =========================
// CIRCULAR OFFSET
// =========================

// 예:
//
// current = 0
//
// card 7은
// 왼쪽 바로 옆으로 처리
//
// card 1은
// 오른쪽 바로 옆으로 처리

function getCircularOffset(
    index
) {

    let offset =
        index - currentIndex;


    const half =
        cards.length / 2;


    if (
        offset > half
    ) {

        offset -=
            cards.length;

    }


    if (
        offset < -half
    ) {

        offset +=
            cards.length;

    }


    return offset;

}




// =========================
// RENDER
// =========================

function render() {

    const stageWidth =
        stage.clientWidth;


    // 화면 크기에 따라
    // 카드 간격 자동 설정

    const spacing =
        Math.min(
            stageWidth * 0.27,
            280
        );


    cards.forEach(
        (
            card,
            index
        ) => {

            const offset =
                getCircularOffset(
                    index
                );


            // 너무 멀리 있는 카드는 숨김

            if (
                Math.abs(
                    offset
                ) > 3
            ) {

                card.style.opacity =
                    "0";


                card.style.pointerEvents =
                    "none";


                return;

            }


            const x =
                offset * spacing
                +
                dragOffset;


            let scale = 1;

            let rotate = 0;

            let opacity = 1;

            let blur = 0;


            // 가운데

            if (
                offset === 0
            ) {

                scale = 1;

                rotate = 0;

                opacity = 1;

            }


            // 좌우 첫 번째

            else if (
                Math.abs(
                    offset
                ) === 1
            ) {

                scale = 0.88;

                rotate =
                    offset > 0
                        ? -13
                        : 13;

                opacity = 0.62;

            }


            // 좌우 두 번째

            else if (
                Math.abs(
                    offset
                ) === 2
            ) {

                scale = 0.76;

                rotate =
                    offset > 0
                        ? -20
                        : 20;

                opacity = 0.3;

                blur = 0.3;

            }


            // 좌우 세 번째

            else {

                scale = 0.68;

                rotate =
                    offset > 0
                        ? -25
                        : 25;

                opacity = 0.08;

                blur = 1;

            }


            card.style.pointerEvents =
                offset === 0
                    ? "auto"
                    : "none";


            card.style.opacity =
                opacity;


            card.style.filter =
                `blur(${blur}px)`;


            card.style.zIndex =
                50 -
                Math.abs(
                    offset
                );


            card.style.transform =

                `translateX(
                    calc(
                        -50% + ${x}px
                    )
                )
                scale(${scale})
                rotateY(${rotate}deg)`;

        }
    );



    // =========================
    // DOT
    // =========================

    dots.forEach(
        (
            dot,
            index
        ) => {

            dot.classList.toggle(
                "active",
                index === currentIndex
            );

        }
    );



    // =========================
    // NUMBER
    // =========================

    currentNumber.textContent =

        String(
            currentIndex + 1
        ).padStart(
            2,
            "0"
        );

}




// =========================
// NEXT
// =========================

function next() {

    currentIndex =

        (
            currentIndex + 1
        )
        %
        cards.length;


    render();

}




// =========================
// PREVIOUS
// =========================

function previous() {

    currentIndex =

        (
            currentIndex
            -
            1
            +
            cards.length
        )
        %
        cards.length;


    render();

}




// =========================
// BUTTONS
// =========================

nextButton.addEventListener(
    "click",
    next
);


prevButton.addEventListener(
    "click",
    previous
);




// =========================
// POINTER DOWN
// =========================

stage.addEventListener(
    "pointerdown",
    event => {

        dragging = true;

        startX =
            event.clientX;

        dragOffset = 0;


        stage.classList.add(
            "dragging"
        );


        stage.setPointerCapture(
            event.pointerId
        );

    }
);




// =========================
// POINTER MOVE
// =========================

stage.addEventListener(
    "pointermove",
    event => {

        if (
            !dragging
        ) {

            return;

        }


        dragOffset =

            event.clientX
            -
            startX;


        render();

    }
);




// =========================
// POINTER UP
// =========================

stage.addEventListener(
    "pointerup",
    event => {

        if (
            !dragging
        ) {

            return;

        }


        dragging = false;


        stage.classList.remove(
            "dragging"
        );


        stage.releasePointerCapture(
            event.pointerId
        );


        // 충분히 왼쪽으로 드래그

        if (
            dragOffset < -60
        ) {

            currentIndex =

                (
                    currentIndex + 1
                )
                %
                cards.length;

        }


        // 충분히 오른쪽으로 드래그

        else if (
            dragOffset > 60
        ) {

            currentIndex =

                (
                    currentIndex
                    -
                    1
                    +
                    cards.length
                )
                %
                cards.length;

        }


        dragOffset = 0;


        render();

    }
);




// =========================
// POINTER CANCEL
// =========================

stage.addEventListener(
    "pointercancel",
    () => {

        dragging = false;

        dragOffset = 0;


        stage.classList.remove(
            "dragging"
        );


        render();

    }
);




// =========================
// KEYBOARD
// =========================

window.addEventListener(
    "keydown",
    event => {

        if (
            event.key ===
            "ArrowRight"
        ) {

            next();

        }


        if (
            event.key ===
            "ArrowLeft"
        ) {

            previous();

        }

    }
);




// =========================
// RESIZE
// =========================

window.addEventListener(
    "resize",
    render
);




// =========================
// INITIAL
// =========================

render();