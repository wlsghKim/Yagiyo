// 다음버튼 클릭시 실행되는 함수
      function nextBtn(defaultGender, defaultName, defaultAge, defaultIllness, defaultDosage, defaultPoint, defaultHeight) {
        const nextInput = document.getElementById('kilogram');
        const nextError = document.getElementById('next-error');
        const inputValue = nextInput.value.trim();
        const bmiValueElement = document.getElementById('bmi-value');
        // 로딩 중... 이라는 메시지를 보여줍니다.
          showLoadingMessage();

        if (inputValue === '') {
          nextError.innerHTML = '답변을 입력하세요';
          return false;
        } else if (!/^\d+$/.test(inputValue)) {
          nextError.innerHTML = '숫자만 입력하세요';
          return false;
        } else if (inputValue < 30 || inputValue > 300) {
          nextError.innerHTML = '30부터 300까지만 입력할 수 있습니다';
          return false;
        } else {

          nextError.innerHTML = '';
          const urlParams = new URLSearchParams(window.location.search);
          const name = urlParams.get('name') || defaultName;
          const gender = urlParams.get('gender') || defaultGender;
          const age = urlParams.get('age') || dufaultAGe;
          let illness = urlParams.get('illness') || defaultIllness;
          let dosage = urlParams.get('dosage') || defaultDosage;
          let point = urlParams.get('point') || defaultPoint;
          const height = urlParams.get('height') || dufaultHeight;
          const weight = inputValue;
          const bmi = calculateBMI(height, weight);

          function calculateBMI(height, weight) {
            // Calculate the BMI
            let bmi = weight / ((height / 100) * (height / 100));
            // Check if bmi is a valid number
            if (!isNaN(bmi)) {
              // Round the BMI to one decimal place
              bmi = bmi.toFixed(1);
            }

            // update the URL with the BMI value
            urlParams.set('bmi', bmi);
            window.history.replaceState(
              {},
              '',
              `${location.pathname}?${urlParams.toString()}`,
            );

            // update the BMI value element
            if (bmiValueElement) {
              bmiValueElement.textContent = bmi;
            }

            // Get the bmi-dot element
            let bmiDot = document.querySelector('.bmi-dot');

            // Update the margin-left value of the bmi-dot based on the calculated bmi value
            if (bmi && bmiDot) {
              let bmiValue = parseFloat(bmi);
              if (bmiValue < 18.5) {
                bmiDot.style.marginLeft = '0%';
              } else if (bmiValue < 23) {
                bmiDot.style.marginLeft = '25%';
              } else if (bmiValue < 25) {
                bmiDot.style.marginLeft = '50%';
              } else if (bmiValue < 30) {
                bmiDot.style.marginLeft = '75%';
              } else {
                bmiDot.style.marginLeft = '100%';
              }
            }

            // navigate to the result page
            let resultURL =
              'http://localhost:9080/result?name=' +
              encodeURIComponent(name) +
              '&gender=' +
              encodeURIComponent(gender) +
              '&age=' +
              encodeURIComponent(age) +
              '&illness=' +
              encodeURIComponent(illness) +
              '&dosage=' +
              encodeURIComponent(dosage) +
              '&point=' +
              encodeURIComponent(point)+
              '&height=' +
              encodeURIComponent(height) +
              '&weight=' +
              encodeURIComponent(weight) +
              '&bmi=' +
              encodeURIComponent(bmi) +
              '&bmiDot=' +
              encodeURIComponent(bmi);

            // Hide the loading screen once the result page is loaded
                window.location.href = resultURL;
                return true;
          }
        }
      }

      // 로딩 중... 이라는 메시지를 보여주는 함수
      function showLoadingMessage() {
        // #overlay라는 id를 가진 div 요소를 만들고, 그 안에 로딩 이미지를 추가합니다.
        let overlay = $("<div id='overlay'><div class='loader'></div></div>");
        // body 요소의 맨 끝에 overlay 요소를 추가합니다.
        $("body").append(overlay);
      }

      // 로딩 중... 이라는 메시지를 숨기는 함수
      function hideLoadingMessage() {
        // #overlay라는 id를 가진 div 요소를 삭제합니다.
        $("#overlay").remove();
      }

      function showLoadingMessage() {
        let overlay = $("<div id='overlay'><div class='loader'></div></div>");
        $("body").append(overlay);

        setTimeout(function() {
          hideLoadingMessage();
        }, 5000);
      }