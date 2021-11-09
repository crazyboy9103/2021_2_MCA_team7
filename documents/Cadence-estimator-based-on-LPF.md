## Cadence estimator based on LPF

#### 2021/11/04

시각 $t_{k}$에 측정한 위치를 $p_{k}$라 하자. 그러면 측정치 역할을 하는 순간 속도 $z_{k}$는
$$
z_{k}=\frac{p_{k} - p_{k-1}}{t_{k} - t_{k-1}}
$$
과 같이 계산할 수 있다. 이전 시각 $t_{k-1}$에서 추정한 속도를 $\hat{v}_{k-1}$​라 할 때, 측정치 업데이트는 다음과 같이 할 수 있다.
$$
\begin{align}
\hat{v}_{k} &= \hat{v}_{k}^{-}+\beta(z_{k} - \hat{v}_{k}^{-}) \\
&= \hat{v}_{k} + \beta(z_{k} - \hat{v}_{k})
\end{align}
$$
여기서, $\hat{v}_{k}^{-}=\hat{v}_{k}$​로 계산할 수 있는 것은 constant velocity with small random acceleration 모델을 가정하기 때문.

