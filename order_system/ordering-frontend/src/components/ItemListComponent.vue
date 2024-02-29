<template>
    <div class="container">
        <div class="page-header text-center" style="margin-top: 20px"><h1>{{pageTitle}}</h1></div>
        <div class="d-flex justify-content-between" style="margin-top:20px;">
            <form @submit.prevent="searchItems">
                <select v-model="searchType" style="display: inline-block; width:auto; margin-right:5px;">  
                <!-- v-model="" - 변수의 키값 -->
                    <option value="optional">선택</option>
                    <option value="name">상품명</option>
                    <option value="category">카테고리</option>
                </select>
                <input type="text" v-model="searchValue"/>
                <!-- v-model="" - 변수의 value값 -->
                <button type="submit" methods>검색</button>
                
            </form>
            <div v-if="!isAdmin">
                <botton @click="addCart" class="btn btn-secondary">장바구니</botton>
                <botton @click="placeOrder" class="btn btn-success">주문하기</botton>
            </div>
            <div v-if="isAdmin">
                <a href="/item/create"><botton class="btn btn-success">상품등록</botton></a>
                
            </div>

        </div>
        <table class="table">
            <thead>
            <!-- checkBox에 대한 th이다 -->
                <th v-if="!isAdmin"></th> 
                <th>제품사진</th>
                <th>제품명</th>
                <th>가격</th>
                <th>재고수량</th>
                <th v-if="!isAdmin">주문수량</th>
                <th v-if="isAdmin">Action</th>
            </thead>
            <tbody>
                <tr v-for="item in itemList" :key="item.id">
                <!-- 체크박스를 선택하면 value가 true가 담기게 됨 -->
                    <td v-if="!isAdmin" ><input type="checkbox" v-model="selectedItems[item.id]"/></td>
                    <td><img :src="getImage(item.id)" style="height: 100px; width:auto;" /></td>
                    <td>{{item.name}}</td>
                    <td>{{item.price}}</td>
                    <td>{{item.stockQuantity}}</td>
                    <td v-if="!isAdmin"><input type="number" min=1 v-model="item.quantity" style="width: 60px"/></td>
                    <td v-if="isAdmin"><button @click="deleteItem(item.id)" class="btn btn-secondary">삭제</button> </td> 
                    <!-- DB데이터 조회해서 없으면 quantity라는 객체를 자바스크립에 들어가버림 -->
                    
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import axios from 'axios';
import {mapActions} from 'vuex';
export default {
    props:['isAdmin', 'pageTitle'],
    data(){
        return{
            itemList: [],
            pageSize: 10,
            currentPage: 0,
            searchType: 'name',
            searchValue: '',
            isLastPage: false,
            isLoading: false,
            selectedItems: {}  // {}: 객체, []: 배열
        }
    },
    created () {
       this.loadItems();
    },
    // mounted : window dom 객체가 생성된 이후에 생성되는 hook함수
    mounted() {
        // scroll 동작이 발생할때마다 scrollPagination함수 호출된다는 의미
        window.addEventListener('scroll', this.scrollPagination)
    },
    methods: {
        ...mapActions(['addToCart']), //action의 addToCart를 가져오기
        addCart(){
            const orderItems = Object.keys(this.selectedItems)
                                .filter(key=>this.selectedItems[key]===true)
                                .map(key => {
                                    const item = this.itemList.find(item =>item.id == key);
                                    return{itemId:item.id, name:item.name ,count:item.quantity};
                                });
            // mutation 직접 호출 방식
            // orderItems.forEach(item =>this.$store.commit('addToCart', item));
            // action호출 방식
            orderItems.forEach(item =>this.$store.commit('addToCart', item));
            this.selectedItems =[];
            
        },
        async placeOrder(){
            // console.log(this.selectedItems);
            // "1": ture,
            // "2":false --> 이런식으로 데이터가 넘어옴
            // Object.keys : 위의 데이터구조에서 1,2등 key값 추출하는 메서드
            // [orderReqItemDtos: {itemId:1, count:10}, {itemId:2, count:10}, {itemId:3, count:10}] --이런식으로 나옴
            const orderItems = Object.keys(this.selectedItems)
                                .filter(key=>this.selectedItems[key]===true)
                                .map(key => {
                                    const item = this.itemList.find(item =>item.id == key);
                                    return{itemId:item.id, count:item.quantity};
                                });
            if(orderItems.length < 1){
                alert("주문대상 물건이 없습니다.")
                return;
            }
            if(!confirm(`${this.getTotalQuantity} 개의 상품을 주문하시겠습니까?`)){
                console.log("주문이 취소되었습니다.");
                return;
            }
            
            const token = localStorage.getItem('token');
            const headers = token ? {Authorization: `Bearer ${token}`} : {};
                                                                                // {orderItems} 이렇게 하면 데이터가
                                                                                // {"orderReqItemDtos": [{"itemId": 1,"count": 10},{"itemId": 2,"count": 20}]}-이렇게 나감
                                                                                // orderItems : 여기 있는 데이터 배열을 그대로 보내는거
                                                                                // [{itemId:1, count:10}, {itemId:2, count:10}] 이렇게 데이터가 나간다.
            try{
                await axios.post(`${process.env.VUE_APP_API_BASE_URL}/order/create`, orderItems ,{headers})
            
                console.log(orderItems);
                alert("주문이 완료되었습니다.");
                window.location.reload();
            }catch(error){
                console.log(error);
                alert("주문이 실패되었습니다..");
            }
        },
        searchItems(){
            this.itemList = [];
            this.selectedItems = [];
            this.currentPage = 0;
            this.isLastPage = false;
            this.loadItems();
        },
        scrollPagination(){
            // innerHeight : 뷰포트 높이를 픽셀단위로 변환
            // scrollY: 스크롤을 통해  Y축을 이동한 거리
            // offsetHeight: 전체 브라우저 창의 높이
            const nearBottom = window.innerHeight + window.scrollY >= document.body.offsetHeight - 200; // 200정도 스크롤 남으면 페이지 불러옴
            if(nearBottom && !this.isLastPage && !this.isLoading){
                this.currentPage++;
                this.loadItems();
            }
        },
        getImage(id){
            return `${process.env.VUE_APP_API_BASE_URL}/item/${id}/image`
        },
        async loadItems(){
            this.isLoading = true;
            try {
            
                const params = { //params가 정해진 이름이여서 params라고만 써야함 - > 파라미터 방식으로 axios요청 가능
                    page: this.currentPage,
                    size: this.pageSize,
                    // [this.searchType]: this.searchValue -같은 결과 나옴
                }
                if(this.searchType === "name"){ // 검색버튼을 누르지 않았는데도 밑에 나옴
                    params.name = this.searchValue;
                }else if(this.searchType === "category"){
                    params.category = this.searchValue;
                }
                console.log("data 호출");
                //response: itemList가 담겨있음
                const response = await axios
                .get(`${process.env.VUE_APP_API_BASE_URL}/items`,{params});  //searchType으로 들어온값? searchValue으로 들어온값 이런형식으로 json으로 들어가 검색됨
                const addItemList = response.data.map(item=>({...item, quantity:1 }))
                if(addItemList.length < this.pageSize){
                    this.isLastPage = true;
                }
                this.itemList = [...this.itemList, ...addItemList];
                
            } catch (error) {
                console.log(error);
            }
        this.isLoading = false;
        },
        async deleteItem(itemId){
            if(confirm("정말로 삭제하시겠습니까?")){
                const token = localStorage.getItem('token');
                const headers = token ? {Authorization: `Bearer ${token}`} : {};
                axios.delete(`${process.env.VUE_APP_API_BASE_URL}/item/${itemId}/delete`,{headers})
                // url로 백엔드에 api요청을 할때마다async쓰기
                alert("상품 삭제완료");
                window.location.reload();
            }
            
        }
    },
}
</script>

<style lang="scss" scoped>

</style>