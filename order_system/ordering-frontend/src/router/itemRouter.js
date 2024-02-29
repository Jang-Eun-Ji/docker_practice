import ItemList from '@/views/ItemList.vue'
import ItemListMangae from '@/views/ItemListManage.vue'
import ItemCreate from '@/views/ItemCreate.vue'
export const itemRoutes = [
    {   
        path: '/items',
        name: 'ItemList',
        component: ItemList,
    },
    {   
        path: '/items/manage',
        name: 'ItemListMangae',
        component: ItemListMangae,
    },
    {   
        path: '/item/create',
        name: 'ItemCreate',
        component: ItemCreate,
    },
    
];