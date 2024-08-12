function toggleForm() {
    const signupForm = document.getElementById('signup-form');
    const signinForm = document.getElementById('signin-form');

    signupForm.classList.toggle('active');
    signinForm.classList.toggle('active');
}

async function signup() {
    const name = document.getElementById('signup-name').value;
    const email = document.getElementById('signup-email').value;
    const password = document.getElementById('signup-password').value;
    console.log(name, email, password);

    const response = await fetch('http://52.194.71.73/api/1.0/user/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            provider: 'native',
            name: name,
            email: email,
            password: password
        })
    });

    if (response.ok) {
        alert('Sign up successful!');
        toggleForm();
    } else {
        const errorData = await response.json();
        alert(`Sign up failed: ${errorData.message}`);
    }
}

async function signin() {
    const email = document.getElementById('signin-email').value;
    const password = document.getElementById('signin-password').value;
    try {
        const response = await fetch('http://52.194.71.73/api/1.0/user/signin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                provider: 'native',
                email: email,
                password: password
            })
        });

        if (response.ok) {
            const data = await response.json();
            const token = data.data.access_token;
            localStorage.setItem('jwt', token);
            alert('Sign in successful!');
        } else {
            const errorData = await response.json();
            alert(`Sign in failed: ${errorData.message}`);
        }
    } catch (error) {
        alert(`Sign in error: ${error.message}`);
    }
}


async function loadUserProfile() {
    const token = localStorage.getItem('jwt');


    try {
        const response = await fetch('http://52.194.71.73/api/1.0/user/profile', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            const user = data.data;
            document.getElementById('user-name').textContent = user.name;
            document.getElementById('user-email').textContent = user.email;
        } else {

            if (!token) {
                alert('No access token found. Please sign in first.');
                return;

            }
            const errorData = await response.json();
            alert(`Failed to load profile: ${errorData.message}`);
            localStorage.removeItem('jwt');
        }


    } catch (error) {
        alert(`Error loading profile: ${error.message}`);
        localStorage.removeItem('jwt');
    }
}

document.addEventListener('DOMContentLoaded', loadUserProfile);
