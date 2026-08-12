<template>
  <nav class="bottom-nav">

    <RouterLink to="/home" class="nav-item">
      <span class="nav-icon">⌂</span>
      <span>Home</span>
    </RouterLink>

    <RouterLink to="/search" class="nav-item">
      <span class="nav-icon">⌕</span>
      <span>Search</span>
    </RouterLink>

    <RouterLink to="/categories" class="nav-item">
      <span class="nav-icon">▦</span>
      <span>Categories</span>
    </RouterLink>

    <RouterLink to="/cart" class="nav-item">
      <span class="nav-icon">🛒</span>
      <span>Cart</span>
    </RouterLink>

    <RouterLink to="/profile" class="nav-item">
      <span class="nav-icon">○</span>
      <span>Profile</span>
    </RouterLink>

  </nav>
</template>


<style scoped>

.bottom-nav {
  position: fixed;

  bottom: 0;
  left: 50%;

  transform: translateX(-50%);

  width: 100%;
  max-width: 520px;

  height: 72px;

  display: flex;
  justify-content: space-around;
  align-items: center;

  background-color: white;

  border-top: 1px solid #e5e7eb;

  padding: 8px 6px 10px;

  z-index: 100;
}


.nav-item {
  flex: 1;

  display: flex;
  flex-direction: column;

  align-items: center;
  justify-content: center;

  gap: 3px;

  color: #9ca3af;

  text-decoration: none;

  font-size: 11px;
  font-weight: 600;
}


.nav-icon {
  font-size: 22px;
}


.router-link-active {
  color: #ff6b00;
}

</style>