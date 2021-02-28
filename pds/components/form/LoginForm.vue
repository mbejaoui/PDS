<template>
  <div style="margin-top: 12rem; width: 100%;">
    <el-row :gutter="24">
      <el-col :span="16"><div class="grid-content bg-purple">
        <img class="signin-image-pro" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5ma0KezoTW2RTZtyxwGcGlEMmdjr4LYIKig&usqp=CAU" />
      </div></el-col>
      <el-col :span="8" :offset="16">
        <div class="form-signin">
          <b>Se connecter à mon espace Pro.</b>
          <br />
          <br/>
          <el-form
            ref="loginForm"
            :model="loginForm"
            :rules="loginRules"
            hide-required-asterisk>

            <el-form-item prop="email">
              <el-input v-model="loginForm.email" placeholder="Entrez votre email ..."></el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                autocomplete="off"
                placeholder="**********"
              >
              </el-input>
            </el-form-item>

            <el-form-item>
              <el-checkbox v-model="loginForm.rememberMe">
                Se souvenir de moi
              </el-checkbox>
              <br/>
              <el-button type="primary" style="width: 100%;" :loading="loading" @click="submitForm()"
                >Connexion
              </el-button>
              <br/>
              <a class="login-form-forgot" href="https://www.wledi.com/mot-de-passe-oublie">
                <small>Mot de passe oublié</small>
              </a>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Merci de remplir votre mot de passe'))
      } else {
        callback()
      }
    }

    return {
      loginForm: {
        email: '',
        password: '',
        rememberMe: true
      },
      loading: false,
      loginRules: {
        email: [
          {
            required: true,
            message: 'Merci de remplir votre email',
            trigger: 'blur'
          },
          {
            type: 'email',
            message: 'Merci de saisir une adresse email valide',
            trigger: ['blur', 'change']
          }
        ],
        password: [{ validator: validatePass, trigger: 'blur' }]
      }
    }
  },
  computed: {
    redirect() {
      return (
        this.$route.query.redirect &&
        decodeURIComponent(this.$route.query.redirect)
      )
    }
  },
  methods: {
    submitForm() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.onSubmit()
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    async onSubmit() {
      let vm = this
      this.loading = true
      try {
        await this.$auth.loginWith('local', {
          data: {
            email: this.loginForm.email,
            password: this.loginForm.password,
            rememberMe : this.loginForm.rememberMe
          }
        }).then(function(response) {
          console.log('Auth Success')
          console.log(vm.$auth)
          console.log(vm.$auth.loggedIn)
          vm.$router.push('/')
        })
      } catch (e) {
          console.log(e.response.status)
        if(e.response.status===401){
        this.$message.error('E-mail ou mot de passe incorrect!')}
        else if (e.response.status===400){
            this.$message.error('La connexion est réservée uniquement au utilisateur pro!')}
      }
      this.loading = false
    }
  }
}
</script>

<style scoped>
.form-signin {
  width: 100%;
  max-width: 400px;
  padding: 15px;
  margin: 0 auto;
  background: white;
  border-radius: 4px;
  padding: 2rem;
  box-shadow: 0 15px 35px rgba(50,50,93,.1),0 5px 15px rgba(0,0,0,.07)!important;
  font-family: 'Comfortaa', cursive;
}
.signin-image-pro {
  position: absolute;
  width: 550px;
  font-family: 'Comfortaa', cursive;
}
</style>