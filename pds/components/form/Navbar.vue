<template>
  <div :class="$auth.$state.loggedIn">
    <el-menu

      mode="horizontal"
      :style="{ lineHeight: '64px' }"
      @select="handleSelect"
    >
      <template v-if="$auth.$state.loggedIn">
        <el-submenu index="4" style="float: right;">
          <template slot="title">
            <i class="el-icon-user"></i>
            <span slot="title" class="submenu-title-wrapper">
              {{$auth.user.user.fullName}}
            </span>
          </template>
          <el-menu-item index="/utilisateurs"><i class="el-icon-user" style="color: #909399;"></i> Utilisateurs</el-menu-item>
          <el-menu-item index="/factures" ><i class="el-icon-document-copy" style="color: #909399;" ></i> Factures</el-menu-item>
          <el-menu-item index="logout"><i class="el-icon-switch-button" style="color: #909399;"></i> Se déconnecter</el-menu-item>
        </el-submenu>
        <template>
          <el-menu-item index="/"  style="float: right;">
            <i class="el-icon-s-home" style="color: #909399;"></i> Tableau de board
          </el-menu-item>
        </template>
      </template>
      <template v-else>
        <el-menu-item index="/login" style="float: right">Connexion</el-menu-item>
      </template>
    </el-menu>
  </div>

</template>

<script>
  import ElButton from "../../node_modules/element-ui/packages/button/src/button";
  export default {
    components: {ElButton},
    data() {
      return {
        activeIndex: '1',
      }
    },

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
      }
    }
  }
</script>

<style lang="scss" scoped>
  .topbar-menu-container {
    &__with-sidebar {
      z-index: 1;
      width: 79%;
      background-color: white;
      position: fixed;
      top: 0 !important;
    }
    z-index: 1;
    top: 0 !important;
    width: 100%;
    background-color: white;
    position: fixed;
  }

  .logo {
    width: 53px;
    background: rgba(255, 255, 255, 0.2);
    margin: 16px 24px 16px 16px;
    float: left;
  }

  .right-button {
    position: relative;
    top: 1px;
    display: inline-block;
    vertical-align: bottom;
    float: right;
    font-family: 'Comfortaa', cursive;
  }

  a {
    color: inherit;
    text-decoration: none;
    font-family: 'Comfortaa', cursive;
  }
</style>
