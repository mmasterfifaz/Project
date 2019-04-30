        function loginPhone(){
            ApiLogin("pakkanat.a", "Bank1234", "5619");
        }
        function logoutPhone(){
            ApiLogout('pakkanat.a', '5619', '')
        }
        function phoneStateChange(status){
            alert(status);
        }