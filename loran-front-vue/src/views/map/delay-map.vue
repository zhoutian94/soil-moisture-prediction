<template>
  <div id="app">
    <div id="map" />
    <button id="floating-button" @click="navigateToDelayCalculation">时延拟合值计算</button>
  </div>

</template>

<script>
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import 'leaflet-curve'
import markerIcon from '@/icons/map/marker.png' // 确保图片资源被正确打包

// Fix for default icon paths
delete L.Icon.Default.prototype._getIconUrl

L.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png')
})

export default {
  name: 'DelayMap',
  mounted() {
    const chinaLocation = [35.8617, 104.1954]
    const map = L.map('map').setView(chinaLocation, 4) // Centered on China

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map)

    // 设置地图边界
    const southWest = L.latLng(18.0, 73.0) // 中国西南角
    const northEast = L.latLng(53.0, 135.0) // 中国东北角
    const bounds = L.latLngBounds(southWest, northEast)

    map.setMaxBounds(bounds)
    map.on('drag', function() {
      map.panInsideBounds(bounds, { animate: false })
    })

    // 添加成都站
    const chengDuMarker = L.marker([30.5728, 104.0668]).addTo(map)
    // 添加成都站的弹出窗口
    chengDuMarker.bindPopup(`
            <b>站点名称：成都站</b>
            <button onclick="window.location.href='/#/export-delay-excel'">历史数据</button>
            <br>
            时间：2024-09-18 12:10:01<br>
            时延：1258.10
        `)
    chengDuMarker.bindTooltip('成都站', { permanent: true, direction: 'top' })

    // Add a marker in 延安
    const yanAnMarker = L.marker([36.5853, 109.4898]).addTo(map)

    // Bind a popup to the 延安 marker
    yanAnMarker.bindPopup(`
            <b>站点名称：延安站</b><br>
             <button onclick="window.location.href='/#/export-delay-excel'">历史数据</button>
            <br>
            时间：2024-09-15 12:00:01<br>
            时延：1420.20
        `)
    yanAnMarker.bindTooltip('延安站', { permanent: true, direction: 'top' })

    // 添加榆林站
    const yuLinMarker = L.marker([38.2852, 109.7499]).addTo(map)
    // 添加榆林站的弹出窗口
    yuLinMarker.bindPopup(`
        <b>站点名称：榆林站</b><br>
        时间：2024-09-15 12:00:01<br>
        时延：1300.50
    `)
    yuLinMarker.bindTooltip('榆林站', { permanent: true, direction: 'top' })

    // 添加西安站
    const xiAnMarker = L.marker([34.3416, 108.9398]).addTo(map)
    // 添加西安站的弹出窗口
    xiAnMarker.bindPopup(`
        <b>站点名称：西安站</b>
        <button onclick="window.location.href='/#/export-delay-excel'">历史数据</button>
        <br>
        时间：2024-09-15 12:00:01<br>
        时延：552.124
    `)
    xiAnMarker.bindTooltip('西安站', { permanent: true, direction: 'top' })

    // 添加商洛站
    const shangLuoMarker = L.marker([33.8704, 109.9404]).addTo(map)
    // 添加商洛站的弹出窗口
    shangLuoMarker.bindPopup(`
        <b>站点名称：商洛站</b><br>
        时间：2024-09-15 12:00:01<br>
        时延：900.40
    `)
    shangLuoMarker.bindTooltip('商洛站', { permanent: true, direction: 'top' })

    // 添加汉中站
    const hanZhongMarker = L.marker([33.0676, 107.0238]).addTo(map)
    // 添加汉中站的弹出窗口
    hanZhongMarker.bindPopup(`
        <b>站点名称：汉中站</b><br>
        时间：2024-09-15 12:00:01<br>
        时延：800.60
    `)
    hanZhongMarker.bindTooltip('汉中站', { permanent: true, direction: 'top' })

    // 添加宝鸡站
    const baoJiMarker = L.marker([34.3618, 107.2373]).addTo(map)
    // 添加宝鸡站的弹出窗口
    baoJiMarker.bindPopup(`
        <b>站点名称：宝鸡站</b><br>
        时间：2024-09-15 12:00:01<br>
        时延：700.70
    `)
    baoJiMarker.bindTooltip('宝鸡站', { permanent: true, direction: 'top' })

    // 添加安康站
    const anKangMarker = L.marker([32.6847, 109.0293]).addTo(map)
    // 添加安康站的弹出窗口
    anKangMarker.bindPopup(`
        <b>站点名称：安康站</b><br>
        时间：2024-09-15 12:00:01<br>
        时延：600.80
    `)
    anKangMarker.bindTooltip('安康站', { permanent: true, direction: 'top' })

    // Add a marker in 敦煌
    const dunHuangMarker = L.marker([40.1421, 94.6612]).addTo(map)

    // Bind a popup to the 敦煌 marker
    dunHuangMarker.bindPopup(`
            <b>站点名称：敦煌站</b><br>
            时间：2024-09-15 12:00:01<br>
            时延：120.20
        `)
    dunHuangMarker.bindTooltip('敦煌站', { permanent: true, direction: 'top' })

    let animationFrameId
    let isAnimating = false
    let curve = null

    // Function to animate a light effect along the curve
    function animateLight() {
      if (isAnimating) return // Prevent multiple animations

      isAnimating = true
      const light = L.circleMarker([36.5853, 109.4898], {
        radius: 5,
        color: 'yellow',
        fillColor: 'yellow',
        fillOpacity: 1
      }).addTo(map)

      let t = 0

      function animate() {
        t += 0.01
        if (t > 1) t = 0

        const latlng = getPointOnCurve(t)
        light.setLatLng(latlng)

        animationFrameId = requestAnimationFrame(animate)
      }

      function getPointOnCurve(t) {
        const p0 = L.latLng(36.5853, 109.4898) // 延安
        const p1 = L.latLng(39.5, 104.5) // 控制点
        const p2 = L.latLng(30.5728, 104.0668) // 成都

        const lat = (1 - t) * (1 - t) * p0.lat + 2 * (1 - t) * t * p1.lat + t * t * p2.lat
        const lng = (1 - t) * (1 - t) * p0.lng + 2 * (1 - t) * t * p1.lng + t * t * p2.lng

        return L.latLng(lat, lng)
      }

      animate()

      // Stop animation and remove curve when popup is closed
      yanAnMarker.on('popupclose', function() {
        cancelAnimationFrame(animationFrameId)
        map.removeLayer(light)
        if (curve) {
          map.removeLayer(curve)
          curve = null // Reset curve variable
        }
        isAnimating = false
      })
    }

    // Add click event to 延安 marker to animate light and show curve
    chengDuMarker.on('click', function() {
      if (!curve) {
        const path = [
          'M', [36.5853, 109.4898], // 延安
          'Q', [39.5, 104.5], [30.5728, 104.0668] // 成都
        ]
        curve = L.curve(path, { color: 'lightblue', weight: 3 }).addTo(map)
      }
      animateLight()
    })

    // 创建自定义的 marker 图标
    const customIcon = L.icon({
      iconUrl: markerIcon, // 替换为自定义图标的路径
      iconSize: [25, 25], // 设置图标尺寸
      iconAnchor: [12, 25], // 设置图标锚点
      popupAnchor: [0, -25] // 设置弹出窗口锚点
    })

    // 添加地图点击事件监听器
    map.on('click', (e) => {
      const { lat, lng } = e.latlng

      // 如果存在当前的 marker，先将其移除
      if (this.currentMarker) {
        map.removeLayer(this.currentMarker)
      }

      // 创建新的 marker 并添加到地图
      this.currentMarker = L.marker([lat, lng], { icon: customIcon }).addTo(map)

      // 绑定弹出窗口
      this.currentMarker.bindPopup(`
        <b>时间：</b>2024-09-15 12:00:01<br>
        <b>时延拟合值：</b>408.10
      `).openPopup()
    })
  }
}
</script>

<style>
html,
body,
#app {
  height: 100%;
  margin: 0;
  padding: 0;
}

#map {
  height: 100vh;
}

#floating-button {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  z-index: 1000;
  /* 确保按钮在地图上方 */
}

#floating-button:hover {
  background-color: #0056b3;
}
</style>
