import React from 'react';
import {Link} from 'react-router-dom';
import './TermsAndConditions.css';

function TermsAndConditions(){
  return (
    <div className="terms-and-conditions-container">
      <h1>Terms and Conditions</h1>
      <p>Welcome to our website! By using our website, you agree to the following terms and conditions:</p>
      <h3>1. Acceptance of Terms</h3>
      <p>By creating an account or using this platform, you agree to comply with and be bound by these terms. If you disagree with any part of these terms, you may not access the service.</p>

      <h3>2. User Accounts</h3>
      <p>When you create an account, you must provide accurate, complete, and current information. You are entirely responsible for maintaining the confidentiality of your password and restricting access to your account.</p>

      <h3>3. Privacy and Data Sharing</h3>
      <p>This platform is designed for family coordination. Information you input (such as names, schedules, or tasks) will be visible to other members linked to your specific family group account.</p>

      <h3>4. Prohibited Uses</h3>
      <p>You agree not to use the platform to store unlawful data, distribute malicious software, or attempt to breach the security systems protecting our databases.</p>

      <h3>5. Limitation of Liability</h3>
      <p>This service is provided on an "AS IS" and "AS AVAILABLE" basis. We do not guarantee that the platform will be 100% error-free or uninterrupted at all times.</p>

      <h3>6. Changes to Terms</h3>
      <p>We reserve the right to update these terms at any time. Continued use of the platform after changes are posted constitutes your acceptance of the updated terms.</p>
      <div className="back-link">
        Back to <Link to="/signup">Signup</Link>
      </div>
    </div>
  )
}

export default TermsAndConditions;