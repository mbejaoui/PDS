export const state = () => {
    return {
      staffs: [],
      isBusy: true,
      error: null
    }
  }
  
  export const getters = {
      STAFFS: state => {
        return state.staffs
      },
      
      IS_BUSY: state => {
        return state.isBusy
      },
    
    ERROR: state => {
        return state.error
      }
  }
  export const mutations = {
    SET_STAFFS: (state, staffs) => {
      state.staffs = staffs
      state.isBusy = false
    },
    
    SET_IS_BUSY: (state, isBusy) => {
      state.isBusy = isBusy
    },
    SET_ERROR: (state, error) => {
      state.error = error
    },
   
  
  }
  
  export const actions =  {
    async FETCH_STAFFS(context) {
      context.commit('SET_IS_BUSY', true)
      try {
        const {data} = await this.$axios.get("/staffs")
        context.commit('SET_STAFFS', data)
      } catch(err) {
        context.commit('SET_IS_BUSY', false)
        context.commit('SET_ERROR', err)
      }
    },
    async ADD_STAFF(context,{payload }) {
      context.commit('SET_IS_BUSY', true)
      try {
        await this.$axios.post('/staffs',payload)
        const {data} = await this.$axios.get("/staffs")
        context.commit('SET_STAFFS', data)
     
      } catch(err) {
        context.commit('SET_IS_BUSY', false)
        context.commit('SET_ERROR', err)
      }
    },
    /*
    async UPDATE_CHILD(context,payload ) {
      context.commit('SET_IS_BUSY', true)
      try {
        await this.$axios.post(/children/${payload._id},payload)
         const {data} = await this.$axios.get("/childrens")
        context.commit('SET_CHILDREN', data)
      } catch(err) {
        context.commit('SET_IS_BUSY', false)
        context.commit('SET_ERROR', err)
      }
    },
    async DELETE_CHILD(context,payload) {
      context.commit('SET_IS_BUSY', true)
      try {
        await this.$axios.delete(/children/deactivate-child/${payload._id})
         const {data} = await this.$axios.get("/childrens")
         context.commit('SET_CHILDREN', data)
      } catch(err) {
        context.commit('SET_IS_BUSY', false)
        context.commit('SET_ERROR', err)
      }
    }
  */
  }