<template>
    <div class="container">
        <div class="page-header text-center" style="margin-top: 20px">
            <h1>회원가입</h1>
        </div>
        <form @submit.prevent="memberCreate">
            <div class="form-group">
                <label>이름: </label>
                <input class="form-control" type="text" v-model="name" >
            </div>
            <div class="form-group">
                <label for="email">email: </label>
                <input class="form-control" type="text" v-model="email">
            </div>
            <div class="form-group">
                <label for="password">비번: </label>
                <input class="form-control" type="text" v-model="password">
            </div>
            <div class="form-group">
                <label for="city">도시: </label>
                <input class="form-control" type="text" v-model="city">
            </div>
            <div class="form-group">
                <label for="street">상세주소: </label>
                <input class="form-control" type="text" v-model="street">
            </div>
            <div class="form-group">
                <label for="zipcode">우편번호: </label>
                <input class="form-control" type="text" v-model="zipcode">
            </div>
            <div class="form-group">
                <button class="btn btn-primary" type="submit" value="가입완료">가입완료</button>
            </div>
            
        </form>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    data(){
        return{
            name: "",
            email: "",
            password: "",
            city: "",
            street: "",
            zipcode: "",
        }
    },
    methods: {
       async memberCreate(){

        try{
            const registerData = {
                name: this.name, 
                email: this.email, 
                password: this.password,
                city: this.city,
                street: this.street,
                zipcode:this.zipcode,
                };
            await axios.post(`${process.env.VUE_APP_API_BASE_URL}`, registerData); 
            // window.location.href = "/login";
            this.$router.push({name : 'Login'});
        
        }catch(error){
            console.log("heer2");
            const error_message = error.response.data.error_message;
            if(error_message){
                console.log(error_message);
                alert(error_message);
            }else{
                console.log(error);
                alert("입력값 확인 필요")
            }
            
        }
        
       }
    }
}
</script>
