import { defineStore } from 'pinia';
import type { UserType } from '@plooks/apis';

export const useLoginStore = defineStore({
    id: "showLogin",
    state: () => {
        return {
            showLogin: false, // 是否显示登录框
            // 登录注册默认数据(UserType)类型
            loginData: {  
                email: "",
                username: "",
                password: "",
            }
            
        }
    },
    actions: {
        setLoginState(val: boolean) {
            this.showLogin = val;
        },
        setLoginData(val: UserType) {
            this.loginData.email = val.email ? val.email : "";
            this.loginData.username = val.username ? val.username : "";
            this.loginData.password = val.password ? val.password : "";
        },
        //清除setLoginData数据
        clearLoginData() {
            this.loginData.email = "";
            this.loginData.username = "";
            this.loginData.password = "";
        }
        
    } 

})
