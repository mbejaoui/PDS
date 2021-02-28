<template>
  <el-row class="sidebar-menu">
    <el-col :span="24">
      <el-menu

        class="sidebar-menu"
        style="background: transparent"
        text-color="#fff"
        active-text-color="#fff"
        @select="handleSelect"
      >
        <div class="text-align-center" style="margin: 2rem 0 1.5rem 0;">
        </div>
        <hr style="border: 1px solid #f6f7fa8a;margin: 0 1rem 0 1rem;" />
        <el-menu-item index="/profil" >
          <i class="el-icon-postcard"></i>
          <span class="element">Profil  </span>
        </el-menu-item>
        <el-menu-item index="/gestionenfants" >
          <i class="el-icon-ship"></i>
          <span class="element">Gestion des enfants</span>
        </el-menu-item>
        <el-menu-item index="/gestionfamilles" >
          <i class="el-icon-user"></i>
          <span class="element">Gestion des familles</span>
        </el-menu-item>
        <el-menu-item index="/gestionstaffs" >
          <i class="el-icon-user"></i>
          <span class="element">Gestion des employées</span>
        </el-menu-item>
        <el-menu-item index="logout">
          <i class="el-icon-switch-button"></i>
          <span class="element">Se déconnecter</span>
        </el-menu-item>
      </el-menu>
    </el-col>
  </el-row>
</template>

<script>

  export default {
    methods: {
      async handleSelect(index) {
        if (index === 'logout') {
          try {
            await this.$auth.logout()
          } catch (e) {
            this.$message.error(
              "Oups! Nous n'avons pas réussi à vous déconnecter."
            )
          }
          return
        }
        if (index === '/index') {
          this.$router.push('/')
          return
        }
        this.$router.push(index)
      },
    }
  }
</script>

<style lang="scss">
  .element {
    font-weight: 600 !important;
    font-size: 0.9rem !important;
  }
  .sidebar-menu {
    background: #7e76ff !important;
    height: 100%;
    opacity: 0.9;
  }
  .rounded-avatar-container {
    font-size: 28px;
    color: #8c939d;
    line-height: 80px;
    text-align: center;
    cursor: pointer;
    margin: 2rem auto 1rem auto;

    img {
      border-radius: 50%;
      box-shadow: 0 2px 12px 0 rgb(0, 0, 0);
      width: 110px;
      height: 110px;
    }
  }

  .el-menu-item,
  .el-menu-item .is-active {
    span {
      font-weight: 100;
      size: 10px;
      font-size: 1.2rem;
      line-height: 1.5;
      color: #fff;
      margin-left: 1rem;
      font-family: 'Comfortaa', cursive;
    }

    i {
      color: #fff;
    }
  }
  .is-active {
    background-color: #7100ff !important;
    color: #fff !important;
  }
  .el-menu-item:hover {
    background-color: #7100ff !important;
    color: #fff !important;
  }
</style>
