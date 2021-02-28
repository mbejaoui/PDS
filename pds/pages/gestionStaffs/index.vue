<template> 
<div>
  <staff-form></staff-form>
  </div>
</template>

<script>
  import StaffForm from '../../components/form/StaffForm'

  export default {
    layout : "default",
    middleware: ['authenticated'],
    components: {
      StaffForm
   }
  }
</script>
