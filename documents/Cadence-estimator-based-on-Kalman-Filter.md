## Cadence estimator based on Kalman Filter

##### 2021/11/04

#### Reference

T. Glad and L. Ljung, 1984, Velocity Estimation from Irregular, Noisy Position Measurements



#### Filter model

필터의 상태변수를 $x_{k}=(p_{k}, v_{k})$​라 하자. 여기에서 $p_{k}$​와 $v_{k}$​는 각각 irregular time interval을 따르는 measurement time instant $t_{k}$​에서의  위치와 속도를 나타낸다. 이 때, measurement time instant $t_{k}$​는 true time instant $\tau_{k}$​​와 다르며, 다음과 같은 관계를 가진다.
$$
t_{k} = \tau_{k} + u_{k}, \quad u_{k} \sim N(\mu_{u}, R_{2})
$$
자세한 것은 논문 참고 (notation은 조금 수정)

필터의 process model은
$$
x_{k + 1} = \begin{bmatrix}
1 & T_{k} \\
0 & 1
\end{bmatrix}x_{k} + \overline{w}_{k}
$$
이며, $T_{k}=t_{k+1} - t_{k}$이고,
$$
\overline{w}_{k} \sim N \left(0, R_{1}
\begin{bmatrix}
T_{k}^{3}/3 & T_{k}^{2}/2 \\
T_{k}^{2}/2 & T_{k}
\end{bmatrix} \right)
$$
이다.

측정치는 $z_{k}=p_{k}$로, 필터의 measurement model은
$$
z_{k} = \begin{bmatrix}
1 & -\mu_{u}
\end{bmatrix}x_{k} + \begin{bmatrix}
0 & -1
\end{bmatrix}\hat{x}_{k}\overline{v}_{k}
$$
이다. 여기에서
$$
\overline{v}_{k} \sim N(0, R_{2})
$$


#### Filter propagation

state transition matrix를 $F_{k}$​, process noise covariance를 $Q_{k}$​ 하면
$$
F_{k} = \begin{bmatrix}
1 & T_{k} \\
0 & 1
\end{bmatrix},
\quad Q_{k} = R_{1} \begin{bmatrix}
T_{k}^{3}/3 & T_{k}^{2}/2 \\
T_{k}^{2}/2 & T_{k}
\end{bmatrix}
$$
propagation은 Kalman filter 식에 따라 다음과 같이 한다.
$$
\hat{x}_{k+1}^{-}=F_{k}\hat{x}_{k}, \quad P_{k+1}^{-}=F_{k}P_{k}F_{k}^{T} + Q_{k}
$$


#### Filter correction

measurement matrix를 $H_{k}$, measurement noise covariance를 $R_{k}$라 하면
$$
H_{k}=\begin{bmatrix}
1 & -\mu_{u}
\end{bmatrix},\quad R_{k} = \begin{bmatrix}
0 & - 1
\end{bmatrix}\hat{x}_{k}^{-}R_{2}(\hat{x}_{k}^{-})^{T}\begin{bmatrix}
0 \\ -1 \end{bmatrix}
$$
correction은 Kalman filter 식에 따라 다음과 같이 한다.
$$
\begin{align}
K_{k} &= P_{k+1}^{-}H_{k}^{T}(H_{k}P_{k+1}^{-}H_{k}^{T} + R_{k}) \\
\hat{x}_{k+1} &= \hat{x}_{k + 1}^{-} + K_{k}(z_{k} - H_{k}\hat{x}_{k + 1}^{-}) \\
P_{k + 1} &= (I - K_{k}H_{k})P_{k + 1}^{-}
\end{align}
$$


#### Parameters

$R_{1}=0.01$, $R_{2}=0.01$​, $\mu_{u}=1$로 결정.

