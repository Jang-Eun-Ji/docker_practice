<template>
    <div class="container">
        <div class="page-header text-center" style="margin-top: 20px">
            <h1>상품등록</h1>
        </div>
        <form @submit.prevent="itemCreate">
            <div class="form-group">
                <label>상품명 </label>
                <input class="form-control" type="text" v-model="name" >
            </div>
            <div class="form-group">
                <label>카테고리 </label>
                <input class="form-control" type="text" v-model="category" >
            </div>
            <div class="form-group">
                <label>가격 </label>
                <input class="form-control" type="number" v-model="price">
            </div>
            <div class="form-group">
                <label>재고수량 </label>
                <input class="form-control" type="number" v-model="stockQuantity"/>
            </div>
            <div class="form-group">
                <label>상품이미지 : </label>
                <!-- @change와 @click 비교 :
                     @click은 요소가 클릭될 때마다 함수 실행
                     @change는 해당 태그의 값이 변할때 함수 실행
                -->
                <input type="file" class="form-control" accept="image/*" @change="fileUpload" />
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
        return {
            name: "",
            category: "",
            price: null,
            stockQuantity: null,
            itemImage: null,
        }
    },
    methods:{
        fileUpload(event){
            // event.target: 이벤트가 발생한 DOM요소를 가르키는 객체
            this.itemImage = event.target.files[0];
            
        },
        async itemCreate(){
            const registerData = new FormData();
            registerData.append("name", this.name);
            registerData.append("category", this.category);
            registerData.append("price", this.price);
            registerData.append("stockQuantity", this.stockQuantity);
            registerData.append("itemImage", this.itemImage);
            const token = localStorage.getItem("token");
            const headers = token ? {Authorization: `Bearer ${token}`} : {}
            await axios.post(`${process.env.VUE_APP_API_BASE_URL}/item/create`, registerData, {headers});
            this.$router.push("/items/manage");
        }
    }
}
</script>

<style lang="scss" scoped>

</style>