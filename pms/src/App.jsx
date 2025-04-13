import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { useEffect, useState } from 'react';
import './App.css'
import {AuthProvider} from './context/AuthProvider'

import Contact from './pages/Contact';
import About from './pages/About';
import Home from './pages/Home';
import AuthPage from './components/AuthPage';
import ProtectedRoute from './components/ProtectedRoute';
import AdminDashboard from './pages/AdminDashboard';
import MemberDashboard from './pages/MemberDashboard';

function App() {
  const [count, setCount] = useState(0)

  return (
    <AuthProvider>
      <Router>
        {/* <Navbar/> */}
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path='/about' element={<About />} />
          <Route path='/contact' element={<Contact />} />
          <Route path="/auth" element={<AuthPage />} />
          <Route element={<ProtectedRoute allowedRoles={["DONOR"]} />}>
            <Route path="/adminDashboard/:adminId" element={<AdminDashboard />} />
          </Route>
          <Route element={<ProtectedRoute allowedRoles={["BENEFICIARY"]} />}>
            <Route path="/memberDashboard/:memberId" element={<MemberDashboard />} />
          </Route>
        </Routes>
      </Router>
    </AuthProvider>
  )
}

export default App
