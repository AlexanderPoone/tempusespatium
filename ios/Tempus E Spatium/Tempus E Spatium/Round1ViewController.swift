//
//  Round1ViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 6/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons

class Round1ViewController: UIViewController {

    @IBOutlet weak var mPlayer1Container: UIView!
    
    var mPlayer1:ExteriorViewController?
        var mPlayer2:ExteriorViewController?
        
    @IBAction func unwindToRound1ViewController(segue: UIStoryboardSegue) {
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        mPlayer1Container.transform =  CGAffineTransform(rotationAngle: .pi)
        
        mPlayer1 = (children.first! as! ExteriorViewController)
        mPlayer2 = (children.last! as! ExteriorViewController)
        mPlayer1!.view!.backgroundColor = UIColor(named: "CosmicLatte")!
    }

}
